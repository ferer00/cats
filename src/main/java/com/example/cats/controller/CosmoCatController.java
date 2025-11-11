package com.example.cats.controller;

import com.example.cats.service.CosmoCatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cosmo-cats")
public class CosmoCatController {

    private final CosmoCatService cosmoCatService;

    public CosmoCatController(CosmoCatService cosmoCatService) {
        this.cosmoCatService = cosmoCatService;
    }

    @GetMapping
    public ResponseEntity<List<String>> getCats() {

        return ResponseEntity.ok(cosmoCatService.getCosmoCats());
    }

    @GetMapping("/special-products")
    public ResponseEntity<List<String>> getSpecialProducts() {

        return ResponseEntity.ok(cosmoCatService.getSpecialKittyProducts());
    }
}