package com.govind.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.govind.services.ProductService;

@RestController
@RequestMapping("/api/admin/products")
@CrossOrigin("*")
public class ProductController {

    @Autowired
    private ProductService productService;

    // CREATE PRODUCT
    @PostMapping("/add")
    public ResponseEntity<?> add(
            @RequestPart("product") String product,
            @RequestPart(value = "file", required = false) MultipartFile file) throws Exception {

        return productService.addProduct(product, file);
    }

    // UPDATE PRODUCT
    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestPart("product") String product,
            @RequestPart(value = "file", required = false) MultipartFile file) throws Exception {

        return productService.updateProduct(id, product, file);
    }

 // GET ALL (Pagination + Search + Filters + Sort)
    @GetMapping
    public ResponseEntity<?> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String brandName,   // ✅ BRAND PARAM ADDED
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer minStock,
            @RequestParam(required = false) Integer maxStock,
            @RequestParam(required = false) Boolean active
    ) {
        return productService.getAllProducts(
                page, size, search, sortBy, sortDir,
                categoryId, brandName,   // ✅ NOW MATCHES SERVICE
                minPrice, maxPrice, minStock, maxStock, active
        );
    }


 // ✅ GET ALL BRANDS FOR DROPDOWN
    @GetMapping("/brands")
    public ResponseEntity<?> getAllBrands() {
        return productService.getAllBrands();
    }


    // GET SINGLE PRODUCT
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    // DELETE PRODUCT
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }

    @GetMapping("/filter")
    public ResponseEntity<?> filter(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brandName,   // ✅ USE HOGA
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Boolean inStock,
            @RequestParam(required = false) Boolean active) {

        return productService.filterProducts(
                name,
                brandName,   
                categoryId,
                minPrice,
                maxPrice,
                inStock,
                active
        );
    }

}
