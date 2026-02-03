package com.dairyedge.backend.service;

import java.util.List;

import com.dairyedge.backend.dtos.*;

public interface CartService {

    CartItemDTO addToCart(Long userId, Long productId, Integer quantity);

    List<CartItemDTO> getUserCart(Long userId);

    CartItemDTO updateCartItem(Long cartItemId, Integer quantity);

    String removeCartItem(Long cartItemId);
}
