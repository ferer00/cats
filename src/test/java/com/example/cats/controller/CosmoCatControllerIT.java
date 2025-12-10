package com.example.cats.controller;

import com.example.cats.featuretoggle.FeatureToggleExtension;
import com.example.cats.featuretoggle.FeatureToggles;
import com.example.cats.featuretoggle.annotation.DisabledFeatureToggle;
import com.example.cats.featuretoggle.annotation.EnabledFeatureToggle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(FeatureToggleExtension.class)
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        LiquibaseAutoConfiguration.class
})
class CosmoCatControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    @EnabledFeatureToggle(FeatureToggles.COSMO_CATS)
    void getCats_ShouldReturnCats_WhenFeatureIsEnabled() throws Exception {
        mockMvc.perform(get("/api/cosmo-cats"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0]").value("Captain Meow"));
    }

    @Test
    @WithMockUser
    @DisabledFeatureToggle(FeatureToggles.KITTY_PRODUCTS)
    void getSpecialProducts_ShouldReturn501_WhenFeatureIsDisabled() throws Exception {
        mockMvc.perform(get("/api/cosmo-cats/special-products"))
                .andExpect(status().isNotImplemented()) // Очікуємо 501
                .andExpect(jsonPath("$.message").value("Feature 'kittyProductsEnabled' is not enabled."));
    }
}