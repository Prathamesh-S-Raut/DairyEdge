package com.dairyedge.backend.dtos;

import com.dairyedge.backend.entities.*;
import jakarta.validation.constraints.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class ProductDTO {

    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be positive")
    private Double price;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock cannot be negative")
    private Integer stockQuantity;

    @NotNull(message = "Category is required")
    private ProductCategory category;

    @NotNull(message = "Supplier ID is required")
    private Long supplierId;
    
}
