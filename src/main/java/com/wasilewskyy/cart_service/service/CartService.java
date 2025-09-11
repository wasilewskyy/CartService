package com.wasilewskyy.cart_service.service;

import com.wasilewskyy.cart_service.model.dto.CartDto;
import com.wasilewskyy.cart_service.model.dto.SelectedOptionDto;

import java.util.List;

public interface CartService{
    CartDto createCart();

    CartDto getCart(String cartId);

    CartDto addItem(String cartId, Long productId, Integer quantity, List<SelectedOptionDto> options);

    CartDto updateItem(String cartId, Long itemId, Integer quantity, List<SelectedOptionDto> options);

    CartDto removeItem(String cartId, Long itemId);

    CartDto clearCart(String cartId);
}