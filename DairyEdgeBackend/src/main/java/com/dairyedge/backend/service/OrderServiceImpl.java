package com.dairyedge.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.dairyedge.backend.custom_exception.BadRequestException;
import com.dairyedge.backend.custom_exception.ResourceNotFoundException;
import com.dairyedge.backend.dtos.OrderItemResponseDTO;
import com.dairyedge.backend.dtos.OrderResponseDTO;
import com.dairyedge.backend.dtos.PaymentDTO;
import com.dairyedge.backend.entities.CartItem;
import com.dairyedge.backend.entities.Order;
import com.dairyedge.backend.entities.OrderItem;
import com.dairyedge.backend.entities.OrderStatus;
import com.dairyedge.backend.entities.Payment;
import com.dairyedge.backend.entities.PaymentMethod;
import com.dairyedge.backend.entities.PaymentStatus;
import com.dairyedge.backend.entities.User;
import com.dairyedge.backend.repositories.CartItemRepository;
import com.dairyedge.backend.repositories.OrderItemRepository;
import com.dairyedge.backend.repositories.OrderRepository;
import com.dairyedge.backend.repositories.PaymentRepository;
import com.dairyedge.backend.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;

    @Override
    public OrderResponseDTO placeOrder(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + userId));

        List<CartItem> cartItems = cartItemRepository.findByUser(user);
        if (cartItems.isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        // 1️⃣ Create order
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.PLACED);

        double totalAmount = cartItems.stream()
                .mapToDouble(ci -> ci.getProduct().getPrice() * ci.getQuantity())
                .sum();

        order.setTotalAmount(totalAmount);

        // IMPORTANT: do NOT reassign order
        orderRepository.saveAndFlush(order);

        // 2️⃣ Create order items
        List<OrderItem> orderItems = cartItems.stream().map(ci -> {
            OrderItem oi = new OrderItem();
            oi.setOrder(order);                 // ✅ works now
            oi.setProduct(ci.getProduct());     // ✅ entity matches
            oi.setQuantity(ci.getQuantity());
            oi.setPrice(ci.getProduct().getPrice());
            return oi;
        }).collect(java.util.stream.Collectors.toList());

        orderItemRepository.saveAll(orderItems);
        order.setOrderItems(orderItems); // 🔥 critical for DTO mapping

        // 3️⃣ Create payment
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(totalAmount);
        payment.setPaymentMethod(PaymentMethod.COD);
        payment.setPaymentStatus(PaymentStatus.PENDING);

        paymentRepository.saveAndFlush(payment);
        order.setPayment(payment); // 🔥 critical

        // 4️⃣ Clear cart
        cartItemRepository.deleteAll(cartItems);

        // 5️⃣ Manual mapping (NOT ModelMapper)
        return mapToDTO(order);
    }



    @Override
    public List<OrderResponseDTO> getOrdersByUser(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found with id: " + userId));

        return orderRepository.findByUser(user).stream()
                .map(this::mapToDTO)
                .collect(java.util.stream.Collectors.toList());
    }


    @Override
    public List<OrderResponseDTO> getAllOrders() {

        return orderRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(java.util.stream.Collectors.toList());
    }


    @Override
    public OrderResponseDTO updateOrderStatus(Long orderId, String status) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found with id: " + orderId));

        order.setOrderStatus(OrderStatus.valueOf(status.toUpperCase()));

        return modelMapper.map(order, OrderResponseDTO.class);
    }
    
    private OrderResponseDTO mapToDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();

        // Simple fields
        dto.setOrderId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setOrderStatus(order.getOrderStatus());

        // Nested user ID
        if (order.getUser() != null) {
            dto.setUserId(order.getUser().getId());
        }

        // Map order items
        if (order.getOrderItems() != null) {
            List<OrderItemResponseDTO> items = order.getOrderItems().stream().map(oi -> {
                OrderItemResponseDTO itemDTO = new OrderItemResponseDTO();
                itemDTO.setId(oi.getId());
                itemDTO.setProductId(oi.getProduct().getId());
                itemDTO.setProductName(oi.getProduct().getName());
                itemDTO.setQuantity(oi.getQuantity());
                itemDTO.setPrice(oi.getPrice());
                return itemDTO;
            }).toList();
            dto.setOrderItems(items);
        }

        // Map payment
        if (order.getPayment() != null) {
            PaymentDTO paymentDTO = new PaymentDTO();
            paymentDTO.setId(order.getPayment().getId());
            paymentDTO.setPaymentMethod(order.getPayment().getPaymentMethod());
            paymentDTO.setPaymentStatus(order.getPayment().getPaymentStatus());
            paymentDTO.setAmount(order.getPayment().getAmount());
            dto.setPayment(paymentDTO);
        }

        return dto;
    }

}

