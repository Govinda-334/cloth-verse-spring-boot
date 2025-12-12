package com.govind.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.govind.models.Category;
import com.govind.repositories.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepo;

    public Category create(Category c) {
        if (categoryRepo.existsByName(c.getName())) {
            throw new RuntimeException("Category already exists !");
        }
        return categoryRepo.save(c);
    }

    public List<Category> getAll() {
        return categoryRepo.findAll();
    }

    public Category update(Long id, Category c) {
        Category existing = categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        existing.setName(c.getName());
  
        return categoryRepo.save(existing);
    }

    public void delete(Long id) {
        if (!categoryRepo.existsById(id))
            throw new RuntimeException("Category not found");

        categoryRepo.deleteById(id);
    }
}
