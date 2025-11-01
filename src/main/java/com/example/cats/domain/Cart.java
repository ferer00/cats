package com.example.cats.domain;


import lombok.Data;
import java.util.ArrayList;
import java.util.List;

@Data
public class Cart {
    private Long id;
    private List<Product> items = new ArrayList<>();
}