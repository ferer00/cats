package com.example.cats.service;


import com.example.cats.dto.ProductDTO;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    ProductDTO create(ProductDTO dto);
    List<ProductDTO> findAll();
    Optional<ProductDTO> findById(Long id);
    ProductDTO update(Long id, ProductDTO dto);
    void delete(Long id);
}