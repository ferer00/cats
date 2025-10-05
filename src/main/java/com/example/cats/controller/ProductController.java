package com.example.cats.controller;


import com.example.cats.dto.ProductDTO;
import com.example.cats.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    // Create
    @PostMapping
    public ResponseEntity<ProductDTO> create(@Valid @RequestBody ProductDTO dto) {
        ProductDTO created = service.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Read all
    @GetMapping
    public List<ProductDTO> all() {
        return service.findAll();
    }

    // Read one
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getOne(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
        try {
            ProductDTO updated = service.update(id, dto);
            return ResponseEntity.ok(updated);
        } catch (NoSuchElementException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}