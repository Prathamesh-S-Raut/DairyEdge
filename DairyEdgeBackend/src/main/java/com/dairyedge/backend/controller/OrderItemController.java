package com.dairyedge.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.dairyedge.backend.service.OrderItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/order-items")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getItemsByOrder(@PathVariable Long orderId){
        return ResponseEntity.ok(orderItemService.getItemsByOrder(orderId));
    }
}
