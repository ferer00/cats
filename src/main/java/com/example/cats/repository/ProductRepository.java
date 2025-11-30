package com.example.cats.repository;

import com.example.cats.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByNameAndCategory(String name, com.example.cats.domain.Category category);

    List<ProductNameProjection> findByCategoryOrderByNameAsc(com.example.cats.domain.Category category);
}

