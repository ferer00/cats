package com.example.cats.featuretoggle.exception;

public class FeatureToggleNotEnabledException extends RuntimeException {

    public FeatureToggleNotEnabledException(String featureName) {
        super("Feature '" + featureName + "' is not enabled.");
    }
}