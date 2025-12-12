package com.govind.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.govind.models.Product;

public interface ProductRepository extends 
JpaRepository<Product, Long>, 
JpaSpecificationExecutor<Product> {

Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
@Query("SELECT DISTINCT p.brandName FROM Product p WHERE p.brandName IS NOT NULL AND p.brandName <> ''")
List<String> findAllDistinctBrands();
}


