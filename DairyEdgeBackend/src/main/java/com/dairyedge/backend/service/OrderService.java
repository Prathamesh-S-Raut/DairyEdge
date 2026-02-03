package com.dairyedge.backend.service;

import java.util.List;

import com.dairyedge.backend.dtos.*;

public interface OrderService {

    OrderResponseDTO placeOrder(Long userId);

    List<OrderResponseDTO> getOrdersByUser(Long userId);

    List<OrderResponseDTO> getAllOrders();

    OrderResponseDTO updateOrderStatus(Long orderId, String status);
}
