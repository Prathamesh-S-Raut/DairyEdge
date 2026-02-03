package com.dairyedge.backend.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class AddToCartDTO {

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}
