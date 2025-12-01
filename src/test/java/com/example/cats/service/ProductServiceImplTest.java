package com.example.cats.service;

import com.example.cats.domain.Category;
import com.example.cats.domain.Product;
import com.example.cats.dto.ProductDTO;
import com.example.cats.mapper.ProductMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceImplTest {

    private ProductServiceImpl service;

    @BeforeEach
    void setUp() {
        ProductMapper mapper = Mappers.getMapper(ProductMapper.class);
        service = new ProductServiceImpl(mapper);
    }

    @Test
    void create_ShouldAddProduct() {
        ProductDTO dto = new ProductDTO();
        dto.setName("Cosmic Star Food");
        dto.setCategory(Category.FOOD);
        dto.setPrice(10.0);
        dto.setQuantity(5);

        ProductDTO created = service.create(dto);

        assertNotNull(created.getId());
        assertEquals("Cosmic Star Food", created.getName());
        assertEquals(3, service.findAll().size()); // бо 2 продукти були дефолтні
    }

    @Test
    void findAll_ShouldReturnInitialProducts() {
        List<ProductDTO> all = service.findAll();
        assertFalse(all.isEmpty());
        assertTrue(all.size() >= 2);
    }

    @Test
    void findById_ShouldReturnProduct_WhenExists() {
        Optional<ProductDTO> found = service.findById(1L);
        assertTrue(found.isPresent());
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotFound() {
        Optional<ProductDTO> found = service.findById(999L);
        assertTrue(found.isEmpty());
    }

    @Test
    void update_ShouldModifyExistingProduct() {
        ProductDTO dto = new ProductDTO();
        dto.setName("Updated Star");
        dto.setCategory(Category.ACCESSORY);
        dto.setPrice(20.0);
        dto.setQuantity(15);

        ProductDTO updated = service.update(1L, dto);

        assertEquals("Updated Star", updated.getName());
    }

    @Test
    void update_ShouldThrow_WhenProductNotFound() {
        ProductDTO dto = new ProductDTO();
        dto.setName("Ghost Product");
        dto.setCategory(Category.FOOD);
        dto.setPrice(5.0);
        dto.setQuantity(1);

        assertThrows(NoSuchElementException.class, () -> service.update(999L, dto));
    }

    @Test
    void delete_ShouldRemoveProduct() {
        service.delete(1L);
        assertTrue(service.findById(1L).isEmpty());
    }
}
