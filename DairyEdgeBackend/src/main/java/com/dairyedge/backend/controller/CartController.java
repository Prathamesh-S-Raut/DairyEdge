package com.dairyedge.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dairyedge.backend.dtos.AddToCartDTO;
import com.dairyedge.backend.dtos.UpdateCartItemDTO;
import com.dairyedge.backend.security.UserPrincipal;
import com.dairyedge.backend.service.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PreAuthorize("hasRole('USER')")
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(
            @AuthenticationPrincipal UserPrincipal user,
            @RequestBody @Valid AddToCartDTO dto) {

        return ResponseEntity.ok(
                cartService.addToCart(
                    user.getUserIdAsLong(),
                    dto.getProductId(),
                    dto.getQuantity()
                )
        );
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<?> getUserCart(
            @AuthenticationPrincipal UserPrincipal user) {

        return ResponseEntity.ok(
                cartService.getUserCart(user.getUserIdAsLong())
        );
    }


    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/item/{cartItemId}")
    public ResponseEntity<?> updateCartItem(
            @PathVariable Long cartItemId,
            @RequestBody @Valid UpdateCartItemDTO dto) {

        return ResponseEntity.ok(
                cartService.updateCartItem(cartItemId, dto.getQuantity())
        );
    }

    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<?> removeCartItem(@PathVariable Long cartItemId) {
        return ResponseEntity.ok(cartService.removeCartItem(cartItemId));
    }
}
