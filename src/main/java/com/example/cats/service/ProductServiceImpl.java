package com.example.cats.service;

import com.example.cats.domain.Product;
import com.example.cats.dto.ProductDTO;
import com.example.cats.mapper.ProductMapper;
import com.example.cats.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    public ProductServiceImpl(ProductRepository repository, ProductMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ProductDTO create(ProductDTO dto) {
        Product product = mapper.toDomain(dto);
        product.setId(null);
        Product saved = repository.save(product);
        return mapper.toDto(saved);
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public List<ProductDTO> findAll() {
        return repository.findAll().stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public Optional<ProductDTO> findById(Long id) {
        return repository.findById(id).map(mapper::toDto);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public ProductDTO update(Long id, ProductDTO dto) {
        Product existing = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Product with id " + id + " not found"));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setCategory(dto.getCategory());
        existing.setPrice(dto.getPrice());
        existing.setQuantity(dto.getQuantity());

        Product saved = repository.save(existing);
        return mapper.toDto(saved);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
