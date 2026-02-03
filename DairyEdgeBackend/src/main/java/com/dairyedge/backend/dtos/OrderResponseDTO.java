package com.dairyedge.backend.dtos;

import java.time.LocalDateTime;
import java.util.List;

import com.dairyedge.backend.entities.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderResponseDTO {

    private Long orderId;
    private LocalDateTime orderDate;
    private double totalAmount;
    private OrderStatus orderStatus;

    private Long userId;

    private List<OrderItemResponseDTO> orderItems;

    private PaymentDTO payment;
}
