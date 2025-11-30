package com.example.cats.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;

public class CosmicWordValidator implements ConstraintValidator<CosmicWordCheck, String> {

    private final Set<String> cosmicWords = Set.of("star", "galaxy", "comet", "cosmic", "planet", "nebula", "astro");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return true;
        String lower = value.toLowerCase();
        return cosmicWords.stream().anyMatch(lower::contains);
    }
}
