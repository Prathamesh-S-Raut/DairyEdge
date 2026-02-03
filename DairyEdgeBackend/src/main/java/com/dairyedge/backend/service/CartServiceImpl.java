package com.dairyedge.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dairyedge.backend.custom_exception.InvalidInputException;
import com.dairyedge.backend.custom_exception.ResourceNotFoundException;
import com.dairyedge.backend.dtos.*;
import com.dairyedge.backend.entities.CartItem;
import com.dairyedge.backend.entities.Product;
import com.dairyedge.backend.entities.User;
import com.dairyedge.backend.repositories.*;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Override
    public CartItemDTO addToCart(Long userId, Long productId, Integer quantity) {

        if (quantity <= 0)
            throw new InvalidInputException("Quantity must be greater than zero");

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        CartItem cartItem = cartItemRepository
                .findByUserAndProduct(user, product)
                .orElseGet(() -> {
                    CartItem ci = new CartItem();
                    ci.setUser(user);
                    ci.setProduct(product);
                    ci.setQuantity(0);
                    return ci;
                });


        cartItem.setQuantity(cartItem.getQuantity() + quantity);

        CartItem saved = cartItemRepository.save(cartItem);

        return mapToDTO(saved);
    }

    @Override
    public List<CartItemDTO> getUserCart(Long userId) {

    	User user = userRepository.findById(userId)
    	        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    	return cartItemRepository.findByUser(user)
    	        .stream()
    	        .map(this::mapToDTO)
    	        .collect(Collectors.toList());


    }

    @Override
    public CartItemDTO updateCartItem(Long cartItemId, Integer quantity) {

        if (quantity <= 0)
            throw new InvalidInputException("Quantity must be greater than zero");

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        cartItem.setQuantity(quantity);

        return mapToDTO(cartItem);
    }

    @Override
    public String removeCartItem(Long cartItemId) {

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        cartItemRepository.delete(cartItem);

        return "Cart item removed successfully";
    }

    private CartItemDTO mapToDTO(CartItem cartItem) {
        CartItemDTO dto = new CartItemDTO();
        dto.setCartItemId(cartItem.getId());
        dto.setProductId(cartItem.getProduct().getId());
        dto.setProductName(cartItem.getProduct().getName());
        dto.setPrice(cartItem.getProduct().getPrice());
        dto.setQuantity(cartItem.getQuantity());
        dto.setTotalPrice(cartItem.getQuantity() * cartItem.getProduct().getPrice());
        return dto;
    }

}
