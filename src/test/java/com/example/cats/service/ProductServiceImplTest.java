package com.example.cats.service;

import com.example.cats.domain.Category;
import com.example.cats.domain.Product;
import com.example.cats.dto.ProductDTO;
import com.example.cats.mapper.ProductMapper;
import com.example.cats.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceImplTest {

    private ProductServiceImpl service;
    private ProductRepository repository;

    @BeforeEach
    void setUp() {
        repository = mock(ProductRepository.class);
        ProductMapper mapper = Mappers.getMapper(ProductMapper.class);
        service = new ProductServiceImpl(repository, mapper);
    }

    @Test
    void create_ShouldSaveProduct() {
        ProductDTO dto = new ProductDTO();
        dto.setName("Cosmic Star Food");
        dto.setCategory(Category.FOOD);
        dto.setPrice(10.0);
        dto.setQuantity(5);

        Product saved = Product.builder()
                .id(1L)
                .name(dto.getName())
                .category(dto.getCategory())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .build();

        when(repository.save(any(Product.class))).thenReturn(saved);

        ProductDTO result = service.create(dto);

        assertEquals("Cosmic Star Food", result.getName());
        assertEquals(1L, result.getId());
        verify(repository).save(any(Product.class));
    }

    @Test
    void findAll_ShouldReturnMappedDtoList() {
        Product p = Product.builder()
                .id(1L)
                .name("Galaxy Milk")
                .category(Category.FOOD)
                .price(5.0)
                .quantity(10)
                .build();

        when(repository.findAll()).thenReturn(List.of(p));

        List<ProductDTO> result = service.findAll();

        assertEquals(1, result.size());
        assertEquals("Galaxy Milk", result.get(0).getName());
    }

    @Test
    void findById_ShouldReturnProduct_WhenExists() {
        Product p = Product.builder()
                .id(1L)
                .name("Galaxy Milk")
                .category(Category.FOOD)
                .price(5.0)
                .quantity(10)
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(p));

        Optional<ProductDTO> result = service.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Galaxy Milk", result.get().getName());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<ProductDTO> result = service.findById(99L);

        assertTrue(result.isEmpty());
    }

    @Test
    void update_ShouldModifyExistingProduct() {
        Product existing = Product.builder()
                .id(1L)
                .name("Old Name")
                .category(Category.FOOD)
                .price(5.0)
                .quantity(1)
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        ProductDTO dto = new ProductDTO();
        dto.setName("Updated Name");
        dto.setCategory(Category.ACCESSORY);
        dto.setPrice(15.0);
        dto.setQuantity(10);

        ProductDTO updated = service.update(1L, dto);

        assertEquals("Updated Name", updated.getName());
        assertEquals(Category.ACCESSORY, updated.getCategory());
        verify(repository).save(any(Product.class));
    }

    @Test
    void update_ShouldThrow_WhenProductNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        ProductDTO dto = new ProductDTO();
        dto.setName("Ghost");
        dto.setCategory(Category.TOY);
        dto.setPrice(3.0);
        dto.setQuantity(1);

        assertThrows(NoSuchElementException.class, () -> service.update(999L, dto));
    }

    @Test
    void delete_ShouldCallRepository() {
        service.delete(1L);
        verify(repository).deleteById(1L);
    }
}

