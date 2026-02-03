package com.dairyedge.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dairyedge.backend.security.UserPrincipal;
import com.dairyedge.backend.service.OrderService;
import com.dairyedge.backend.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/place")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> placeOrder(@AuthenticationPrincipal UserPrincipal principal){
        Long userId = Long.parseLong(principal.getUserId());
        return ResponseEntity.ok(orderService.placeOrder(userId));
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> getUserOrders(@AuthenticationPrincipal UserPrincipal principal){
        Long userId = Long.parseLong(principal.getUserId());
        return ResponseEntity.ok(orderService.getOrdersByUser(userId));
    }


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> getAllOrders(){
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{orderId}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestParam String status){
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId, status));
    }
}
