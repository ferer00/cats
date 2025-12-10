package com.example.cats.security;

import com.example.cats.AbstractIt;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "security.api.key=super-secret-key"
})
class SecurityJwtIT extends AbstractIt {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getProducts_WithJwtAdminRole_ShouldBeOk() throws Exception {
        mockMvc.perform(
                        get("/api/products")
                                .with(jwt().authorities(createAuthorityList("ROLE_ADMIN")))
                )
                .andExpect(status().isOk());
    }

    @Test
    void getProducts_WithoutAuth_ShouldBeUnauthorized() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getProducts_WithValidApiKey_ShouldBeOk() throws Exception {
        mockMvc.perform(
                        get("/api/products")
                                .header("X-API-KEY", "super-secret-key")
                )
                .andExpect(status().isOk());
    }

    @Test
    void getProducts_WithInvalidApiKey_ShouldBeUnauthorized() throws Exception {
        mockMvc.perform(
                        get("/api/products")
                                .header("X-API-KEY", "wrong-key")
                )
                .andExpect(status().isUnauthorized());
    }
}

