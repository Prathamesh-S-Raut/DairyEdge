package com.dairyedge.backend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemDTO {

    private Long cartItemId;
    private Long productId;
    private String productName;
    private Double price;
    private Integer quantity;
    private Double totalPrice;
}
