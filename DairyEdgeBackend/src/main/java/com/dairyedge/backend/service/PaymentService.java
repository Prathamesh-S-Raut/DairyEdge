package com.dairyedge.backend.service;

import com.dairyedge.backend.dtos.PaymentResponseDTO;
import com.dairyedge.backend.dtos.PaymentVerifyRequestDTO;

public interface PaymentService {

    Object initiatePayment(Long orderId, String method);

    void verifyRazorpayPayment(Long orderId, PaymentVerifyRequestDTO dto);

    PaymentResponseDTO getPaymentById(Long paymentId);
}
