package com.dairyedge.backend.dtos;

import lombok.Data;

@Data
public class PaymentVerifyRequestDTO {

    private String razorpayOrderId;
    private String razorpayPaymentId;
    private String razorpaySignature;
}
