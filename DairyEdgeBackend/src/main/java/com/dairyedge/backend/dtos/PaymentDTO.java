package com.dairyedge.backend.dtos;

import com.dairyedge.backend.entities.PaymentMethod;
import com.dairyedge.backend.entities.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentDTO {

    private Long id;

    private Long orderId;

    private double amount;

    private PaymentMethod paymentMethod;

    private PaymentStatus paymentStatus;

    private String transactionId;
}
