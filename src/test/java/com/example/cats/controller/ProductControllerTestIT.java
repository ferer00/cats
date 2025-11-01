package com.example.cats.controller;

import com.example.cats.domain.Category;
import com.example.cats.dto.ProductDTO;
import com.example.cats.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTestIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductService service;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public ProductService productService() {
            return Mockito.mock(ProductService.class);
        }
    }

    @Test
    void create_ShouldReturnCreated() throws Exception {
        ProductDTO dto = new ProductDTO();
        dto.setId(1L);
        dto.setName("Star Food");
        dto.setCategory(Category.FOOD);
        dto.setPrice(10.0);
        dto.setQuantity(2);

        Mockito.when(service.create(any())).thenReturn(dto);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "name":"Star Food",
                                "category":"FOOD",
                                "price":10.0,
                                "quantity":2
                            }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Star Food"));
    }

    @Test
    void getOne_ShouldReturnProduct_WhenExists() throws Exception {
        ProductDTO dto = new ProductDTO();
        dto.setId(1L);
        dto.setName("Galaxy Toy");
        dto.setCategory(Category.TOY);
        dto.setPrice(5.0);
        dto.setQuantity(3);

        Mockito.when(service.findById(1L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Galaxy Toy"));
    }

    @Test
    void getOne_ShouldReturnNotFound_WhenMissing() throws Exception {
        Mockito.when(service.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/products/99"))
                .andExpect(status().isNotFound());
    }
}
