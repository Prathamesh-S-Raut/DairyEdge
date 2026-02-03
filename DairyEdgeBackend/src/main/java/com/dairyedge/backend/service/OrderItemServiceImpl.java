package com.dairyedge.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dairyedge.backend.custom_exception.ResourceNotFoundException;
import com.dairyedge.backend.dtos.OrderItemResponseDTO;
import com.dairyedge.backend.entities.Order;
import com.dairyedge.backend.entities.OrderItem;
import com.dairyedge.backend.repositories.OrderItemRepository;
import com.dairyedge.backend.repositories.OrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<OrderItemResponseDTO> getItemsByOrder(Long orderId) {

        // Fetch order, throw exception if not found
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));

        // Fetch order items
        List<OrderItem> orderItems = orderItemRepository.findByOrder(order);

        // Map to DTOs using ModelMapper
        return orderItems.stream()
                .map(item -> {
                    OrderItemResponseDTO dto = modelMapper.map(item, OrderItemResponseDTO.class);
                    dto.setProductId(item.getProduct().getId());
                    dto.setProductName(item.getProduct().getName());
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
