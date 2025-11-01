package com.example.cats.service;


import com.example.cats.domain.Product;
import com.example.cats.dto.ProductDTO;
import com.example.cats.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final Map<Long, Product> repo = new ConcurrentHashMap<>();
    private final AtomicLong idCounter = new AtomicLong(1);
    private final ProductMapper mapper;

    public ProductServiceImpl(ProductMapper mapper) {
        this.mapper = mapper;

        var p1 = Product.builder()
                .id(idCounter.getAndIncrement())
                .name("Star Yarn Ball")
                .description("Antigravity yarn - perfect for zero-g naps.")
                .category(com.example.cats.domain.Category.ACCESSORY)
                .price(19.99)
                .quantity(42)
                .build();
        var p2 = Product.builder()
                .id(idCounter.getAndIncrement())
                .name("Galaxy Milk")
                .description("Rich cosmic milk for interstellar kittens.")
                .category(com. example. cats. domain. Category.FOOD)
                .price(5.5)
                .quantity(200)
                .build();
        repo.put(p1.getId(), p1);
        repo.put(p2.getId(), p2);
    }

    @Override
    public ProductDTO create(ProductDTO dto) {
        Product p = mapper.toDomain(dto);
        p.setId(idCounter.getAndIncrement());
        repo.put(p.getId(), p);
        return mapper.toDto(p);
    }

    @Override
    public List<ProductDTO> findAll() {
        return repo.values().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public Optional<ProductDTO> findById(Long id) {
        return Optional.ofNullable(repo.get(id)).map(mapper::toDto);
    }

    @Override
    public ProductDTO update(Long id, ProductDTO dto) {
        Product existing = repo.get(id);
        if (existing == null) {
            throw new NoSuchElementException("Product with id " + id + " not found");
        }
        Product updated = mapper.toDomain(dto);
        updated.setId(id);
        repo.put(id, updated);
        return mapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        repo.remove(id);
    }
}
