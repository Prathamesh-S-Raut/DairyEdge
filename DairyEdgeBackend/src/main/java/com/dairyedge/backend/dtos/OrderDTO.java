package com.dairyedge.backend.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
public class OrderDTO {

    @NotBlank(message = "Payment method is required")
    private String paymentMethod;   // UPI, CARD, COD

    @NotBlank(message = "Delivery address is required")
    private String deliveryAddress;
}
