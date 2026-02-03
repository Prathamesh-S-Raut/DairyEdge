package com.dairyedge.backend.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dairyedge.backend.custom_exception.PaymentProcessingException;
import com.dairyedge.backend.dtos.PaymentVerifyRequestDTO;
import com.dairyedge.backend.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    // USER chooses COD or ONLINE
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/initiate/{orderId}")
    public ResponseEntity<?> initiatePayment(
            @PathVariable Long orderId,
            @RequestParam String method) {

        return ResponseEntity.ok(
                paymentService.initiatePayment(orderId, method)
        );
    }

    // Razorpay verification
    @PostMapping("/razorpay/verify/{orderId}")
    public ResponseEntity<?> verifyRazorpayPayment(
            @PathVariable Long orderId,
            @RequestBody PaymentVerifyRequestDTO dto) {

        try {
            paymentService.verifyRazorpayPayment(orderId, dto);
            return ResponseEntity.ok(
                    Map.of("status", "success", "message", "Payment verified successfully")
            );
        } catch (PaymentProcessingException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("status", "error", "message", e.getMessage())
            );
        }
    }


    // ADMIN: view payment details
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{paymentId}")
    public ResponseEntity<?> getPaymentDetails(@PathVariable Long paymentId) {
        return ResponseEntity.ok(
                paymentService.getPaymentById(paymentId)
        );
    }
}
