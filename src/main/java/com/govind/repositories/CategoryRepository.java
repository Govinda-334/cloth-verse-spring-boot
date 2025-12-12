package com.govind.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.govind.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
}
