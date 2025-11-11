package com.example.cats.service;

import com.example.cats.featuretoggle.FeatureToggles;
import com.example.cats.featuretoggle.annotation.FeatureToggle;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CosmoCatService {

    @FeatureToggle(FeatureToggles.COSMO_CATS)
    public List<String> getCosmoCats() {

        return List.of("Captain Meow", "Astro-Whiskers", "Nebula Paws");
    }

    @FeatureToggle(FeatureToggles.KITTY_PRODUCTS)
    public List<String> getSpecialKittyProducts() {

        return List.of("Moon-Rock Scratcher", "Comet-Tail Toy");
    }
}