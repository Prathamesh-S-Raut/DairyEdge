package com.dairyedge.backend.service;

import java.util.List;

import com.dairyedge.backend.dtos.OrderItemResponseDTO;

public interface OrderItemService {

    List<OrderItemResponseDTO> getItemsByOrder(Long orderId);
}
