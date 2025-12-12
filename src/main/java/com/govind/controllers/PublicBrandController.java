package com.govind.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.govind.services.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/public/brands")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PublicBrandController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<?> getAllBrands() {
        return productService.getAllBrands();
    }
}
