package com.govind.services;
	
	import java.io.IOException;
	import java.nio.file.Files;
	import java.nio.file.Path;
	import java.nio.file.Paths;
	import java.util.HashMap;
	import java.util.List;
	import java.util.Map;
	
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.data.domain.Page;
	import org.springframework.data.domain.PageRequest;
	import org.springframework.data.domain.Pageable;
	import org.springframework.data.domain.Sort;
	import org.springframework.http.HttpStatus;
	import org.springframework.http.ResponseEntity;
	import org.springframework.stereotype.Service;
	import org.springframework.web.multipart.MultipartFile;
	
	import com.govind.models.Category;
	import com.govind.models.Product;
	import com.govind.repositories.CategoryRepository;
	import com.govind.repositories.ProductRepository;
	import com.govind.response.MyResponseWrapper;
	import com.govind.specification.ProductSpecification;
	
	import tools.jackson.databind.ObjectMapper;
	
	import org.springframework.data.jpa.domain.Specification;
	
	@Service
	public class ProductService {
	
	    @Autowired
	    private ProductRepository productRepo;
	
	    @Autowired
	    private CategoryRepository categoryRepo;
	
	    @Autowired
	    private MyResponseWrapper response;
	
	    private static final String UPLOAD_DIR =
	            System.getProperty("user.dir") + "/uploads/products/";
	
	    // ✔ Universal response wrapper
	    private ResponseEntity<?> universal(String message, Object data, HttpStatus status) {
	        response.setMessage(message);
	        response.setData(data);
	        return new ResponseEntity<>(response, status);
	    }
	
	    // ------------------ ADD PRODUCT (MULTIPART) --------------------
	    public ResponseEntity<?> addProduct(String productJson, MultipartFile file)
	            throws IOException {
	
	        ObjectMapper mapper = new ObjectMapper();
	        Product product = mapper.readValue(productJson, Product.class);
	
	        // Get category
	        Long catId = product.getCategory().getId();
	        Category category = categoryRepo.findById(catId)
	                .orElseThrow(() -> new RuntimeException("Category not found"));
	
	        // Save file
	        String fileName = file.getOriginalFilename();
	        Path path = Paths.get(UPLOAD_DIR, fileName);
	        Files.createDirectories(path.getParent());
	        Files.write(path, file.getBytes());
	
	        // Build product
	        product.setImagePath(fileName);
	        product.setCategory(category);
	
	        Product saved = productRepo.save(product);
	        return universal("Product added successfully", saved, HttpStatus.CREATED);
	    }
	
	    // ------------------ UPDATE PRODUCT ----------------
	    public ResponseEntity<?> updateProduct(
	            Long productId,
	            String productJson,
	            MultipartFile file
	    ) throws IOException {
	
	        Product existing = productRepo.findById(productId)
	                .orElseThrow(() -> new RuntimeException("Product not found"));
	
	        ObjectMapper mapper = new ObjectMapper();
	        Product product = mapper.readValue(productJson, Product.class);
	
	        // Category update
	        Long catId = product.getCategory().getId();
	        Category category = categoryRepo.findById(catId)
	                .orElseThrow(() -> new RuntimeException("Category not found"));
	
	        // Save new file
	        String fileName = file.getOriginalFilename();
	        Path path = Paths.get(UPLOAD_DIR, fileName);
	        Files.createDirectories(path.getParent());
	        Files.write(path, file.getBytes());
	
	        // Assign updated data
	        product.setId(productId);
	        product.setCategory(category);
	        product.setImagePath(fileName);
	
	        Product updated = productRepo.save(product);
	        return universal("Product updated successfully", updated, HttpStatus.OK);
	    }
	
	    // ------------------ GET ALL WITH PAGINATION ----------------
	    public ResponseEntity<?> getAllProducts(
	            int page,
	            int size,
	            String search,
	            String sortBy,
	            String sortDir,
	            Long categoryId,
	            String brandName,      // ✅ NEW
	            Double minPrice,
	            Double maxPrice,
	            Integer minStock,
	            Integer maxStock,
	            Boolean active) {

	        // ✅ Create sort
	        Sort sort = sortDir.equalsIgnoreCase("desc")
	                ? Sort.by(sortBy).descending()
	                : Sort.by(sortBy).ascending();

	        // ✅ Create pageable
	        Pageable pageable = PageRequest.of(page, size, sort);

	        // ✅ ✅ ✅ FINAL SPECIFICATION CHAIN (FIXED + BRAND ADDED)
	        Specification<Product> spec = Specification
	                .where(ProductSpecification.nameContains(search))
	                .and(ProductSpecification.hasCategory(categoryId))
	                .and(ProductSpecification.hasBrandName(brandName))  // ✅ BRAND FILTER
	                .and(ProductSpecification.priceBetween(minPrice, maxPrice))
	                .and(ProductSpecification.stockBetween(minStock, maxStock))
	                .and(ProductSpecification.isActive(active));        // ✅ BRACKET FIXED

	        // ✅ Get paginated data
	        Page<Product> productPage = productRepo.findAll(spec, pageable);

	        if (productPage.isEmpty()) {
	            return universal("No products found", null, HttpStatus.NOT_FOUND);
	        }

	        // ✅ Pagination response
	        Map<String, Object> responseData = new HashMap<>();
	        responseData.put("products", productPage.getContent());
	        responseData.put("currentPage", productPage.getNumber());
	        responseData.put("totalItems", productPage.getTotalElements());
	        responseData.put("totalPages", productPage.getTotalPages());
	        responseData.put("pageSize", size);
	        responseData.put("hasNext", productPage.hasNext());
	        responseData.put("hasPrevious", productPage.hasPrevious());

	        return universal("Products found", responseData, HttpStatus.OK);
	    }
	 // ------------------ GET ALL DISTINCT BRANDS ----------------
	    public ResponseEntity<?> getAllBrands() {
	        List<String> brands = productRepo.findAllDistinctBrands();
	        return universal("Brands found", brands, HttpStatus.OK);
	    }


	    // ------------------ GET BY ID ----------------
	    public ResponseEntity<?> getProductById(Long id) {
	        Product p = productRepo.findById(id)
	                .orElse(null);
	
	        if (p == null) {
	            return universal("Product not found", null, HttpStatus.NOT_FOUND);
	        }
	
	        return universal("Product found", p, HttpStatus.OK);
	    }
	
	    // ------------------ DELETE ----------------
	    public ResponseEntity<?> deleteProduct(Long id) {
	        Product p = productRepo.findById(id).orElse(null);
	
	        if (p == null) {
	            return universal("No product with ID " + id, null, HttpStatus.NOT_FOUND);
	        }
	
	        productRepo.deleteById(id);
	        return universal("Product deleted successfully", null, HttpStatus.OK);
	    }
	
	    // ------------------ FILTER (SPECIFICATION) - Legacy ----------------
	    public ResponseEntity<?> filterProducts(
	            String name,
	            String brandName,     // ✅ BRAND PARAM ADDED
	            Long categoryId,
	            Double minPrice,
	            Double maxPrice,
	            Boolean inStock,
	            Boolean active) {

	        Specification<Product> spec = Specification.where(ProductSpecification.nameContains(name))
	                .and(ProductSpecification.hasBrandName(brandName))   
	                .and(ProductSpecification.hasCategory(categoryId))
	                .and(ProductSpecification.priceBetween(minPrice, maxPrice))
	                .and(ProductSpecification.inStock(inStock))
	                .and(ProductSpecification.isActive(active));

	        List<Product> filtered = productRepo.findAll(spec);

	        return universal("Filtered products found", filtered, HttpStatus.OK);
	    }

	}