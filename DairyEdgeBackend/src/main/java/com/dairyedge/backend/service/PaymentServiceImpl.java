package com.dairyedge.backend.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dairyedge.backend.custom_exception.PaymentProcessingException;
import com.dairyedge.backend.custom_exception.ResourceNotFoundException;
import com.dairyedge.backend.dtos.PaymentResponseDTO;
import com.dairyedge.backend.dtos.PaymentVerifyRequestDTO;
import com.dairyedge.backend.dtos.RazorpayOrderResponseDTO;
import com.dairyedge.backend.entities.Order;
import com.dairyedge.backend.entities.OrderStatus;
import com.dairyedge.backend.entities.Payment;
import com.dairyedge.backend.entities.PaymentMethod;
import com.dairyedge.backend.entities.PaymentStatus;
import com.dairyedge.backend.repositories.OrderRepository;
import com.dairyedge.backend.repositories.PaymentRepository;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final RazorpayClient razorpayClient;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    // ============================
    // INITIATE PAYMENT
    // ============================
    @Override
    public Object initiatePayment(Long orderId, String method) {

        // Fetch order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order with ID " + orderId + " not found"));

        // Convert method to uppercase for consistency and final for lambda capture
        final String paymentMethod = method.toUpperCase();

        // Create payment if not exists
        Payment payment = paymentRepository.findByOrder(order)
                .orElseGet(() -> {
                    Payment p = new Payment();
                    // Set payment method based on user selection
                    p.setPaymentMethod("COD".equals(paymentMethod) ? PaymentMethod.COD : PaymentMethod.ONLINE);
                    p.setPaymentStatus(PaymentStatus.PENDING);
                    p.setOrder(order);
                    p.setAmount(order.getTotalAmount());
                    return paymentRepository.save(p);
                });

        // =========================
        // COD Payment
        // =========================
        if ("COD".equals(paymentMethod)) {
            // For COD, you can optionally confirm the order immediately
            order.setOrderStatus(OrderStatus.CONFIRMED);
            orderRepository.save(order);

            return new PaymentResponseDTO(
                    payment.getId(),
                    order.getId(),
                    payment.getAmount(),
                    payment.getPaymentMethod(),
                    payment.getPaymentStatus(),
                    payment.getTransactionId()
            );
        }

        // =========================
        // ONLINE Payment
        // =========================
        if ("ONLINE".equals(paymentMethod)) {
            try {
                JSONObject options = new JSONObject();
                options.put("amount", (int) (order.getTotalAmount() * 100)); // Razorpay amount in paise
                options.put("currency", "INR");
                options.put("receipt", "order_" + order.getId());

                com.razorpay.Order razorpayOrder = razorpayClient.orders.create(options);

                return new RazorpayOrderResponseDTO(
                        razorpayOrder.get("id"),
                        order.getTotalAmount(),
                        razorpayKeyId
                );

            } catch (Exception e) {
                throw new PaymentProcessingException("Failed to create Razorpay order for Order ID " + orderId, e);
            }
        }

        // =========================
        // Unsupported payment method
        // =========================
        throw new PaymentProcessingException("Unsupported payment method: " + paymentMethod);
    }



    // ============================
    // VERIFY PAYMENT
    // ============================
    @Override
    public void verifyRazorpayPayment(Long orderId, PaymentVerifyRequestDTO dto) {

        // Fetch order
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        // Fetch payment linked to order
        Payment payment = paymentRepository.findByOrder(order)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));

        // Check if payment already verified
        if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
            throw new PaymentProcessingException("Payment already verified");
        }

        try {
            // Create JSONObject directly from DTO fields
            JSONObject attributes = new JSONObject();
            attributes.put("razorpay_order_id", dto.getRazorpayOrderId());
            attributes.put("razorpay_payment_id", dto.getRazorpayPaymentId());
            attributes.put("razorpay_signature", dto.getRazorpaySignature());

            // Verify payment signature using Razorpay Utils
            Utils.verifyPaymentSignature(attributes, razorpayKeySecret);

            // If verification succeeds, update payment and order status
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            payment.setTransactionId(dto.getRazorpayPaymentId());

            order.setOrderStatus(OrderStatus.CONFIRMED);

            paymentRepository.save(payment);
            orderRepository.save(order);

        } catch (Exception e) {
            // If verification fails, mark payment as FAILED
            payment.setPaymentStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            throw new PaymentProcessingException("Payment verification failed");
        }
    }
    
    @Override
    public PaymentResponseDTO getPaymentById(Long paymentId) {

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        return new PaymentResponseDTO(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getAmount(),          // double
                payment.getPaymentMethod(),
                payment.getPaymentStatus(),
                payment.getTransactionId()
        );
    }

}
