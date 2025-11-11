package com.example.cats.controller.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "feature")
public class FeatureToggleProperties {


    private Map<String, Boolean> toggles = new HashMap<>();
}