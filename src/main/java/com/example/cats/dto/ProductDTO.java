package com.example.cats.dto;



import com.example.cats.domain.Category;
import com.example.cats.validation.CosmicWordCheck;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductDTO {
    private Long id;

    @NotNull(message = "name must not be null")
    @Size(min = 3, max = 100, message = "name length must be between 3 and 100")
    @CosmicWordCheck
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull(message = "category must not be null")
    private Category category;

    @NotNull(message = "price must not be null")
    @Min(value = 1, message = "price must be greater than 0")
    private Double price;

    @NotNull(message = "quantity must not be null")
    @Min(value = 0, message = "quantity must be >= 0")
    private Integer quantity;
}