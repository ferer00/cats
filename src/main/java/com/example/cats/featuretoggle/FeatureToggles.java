package com.example.cats.featuretoggle;

import lombok.Getter;

@Getter
public enum FeatureToggles {

    COSMO_CATS("cosmoCatsEnabled"),
    KITTY_PRODUCTS("kittyProductsEnabled");

    private final String featureName;

    FeatureToggles(String featureName) {
        this.featureName = featureName;
    }
}
