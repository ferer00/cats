package com.example.cats.controller;

import com.example.cats.AbstractIt;
import com.example.cats.domain.Category;
import com.example.cats.dto.ProductDTO;
import com.example.cats.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@DisplayName("Product Controller Integration Tests (REAL DB, TestContainers)")
public class ProductControllerTestIT extends AbstractIt {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ProductRepository productRepository;

    private ProductDTO baseDto;

    @BeforeEach
    void init() {

        productRepository.deleteAll();

        baseDto = new ProductDTO();
        baseDto.setName("Comet Food");
        baseDto.setCategory(Category.FOOD);
        baseDto.setPrice(10.5);
        baseDto.setQuantity(7);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createProduct_ShouldReturnCreated() throws Exception {
        mockMvc.perform(
                        post("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(baseDto))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Comet Food"))
                .andExpect(jsonPath("$.category").value("FOOD"))
                .andExpect(jsonPath("$.price").value(10.5))
                .andExpect(jsonPath("$.quantity").value(7));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getAllProducts_ShouldReturnList() throws Exception {
        mockMvc.perform(
                post("/api/products")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(baseDto))
        ).andExpect(status().isCreated());

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Comet Food"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getProductById_ShouldReturnProduct() throws Exception {
        String response = mockMvc.perform(
                        post("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(baseDto))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ProductDTO saved = mapper.readValue(response, ProductDTO.class);

        mockMvc.perform(get("/api/products/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Comet Food"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteProduct_ShouldReturnNoContent() throws Exception {
        String response = mockMvc.perform(
                        post("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(baseDto))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ProductDTO saved = mapper.readValue(response, ProductDTO.class);

        mockMvc.perform(delete("/api/products/" + saved.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/products/" + saved.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createProduct_InvalidData_ShouldReturnBadRequest() throws Exception {
        ProductDTO invalid = new ProductDTO();
        invalid.setName("A");
        invalid.setCategory(null);
        invalid.setPrice(-1.0);
        invalid.setQuantity(-10);

        mockMvc.perform(
                        post("/api/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(invalid))
                )
                .andExpect(status().isBadRequest());
    }
}
