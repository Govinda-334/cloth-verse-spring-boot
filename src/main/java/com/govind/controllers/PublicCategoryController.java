package com.govind.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.govind.models.Category;
import com.govind.services.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/public/categories")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PublicCategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<Category> all() {
        return categoryService.getAll();
    }
}
