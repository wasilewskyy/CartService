package com.wasilewskyy.cart_service.controller;

import com.wasilewskyy.cart_service.model.dto.CartDto;
import com.wasilewskyy.cart_service.model.dto.SelectedOptionDto;
import com.wasilewskyy.cart_service.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping
    public CartDto createCart() {
        return cartService.createCart();
    }

    @GetMapping("/{cartId}")
    public CartDto getCart(@PathVariable String cartId) {
        return cartService.getCart(cartId);
    }

    @PostMapping("/{cartId}/items")
    public CartDto addItem(
            @PathVariable String cartId,
            @RequestParam Long productId,
            @RequestParam(defaultValue = "1") Integer quantity,
            @RequestBody(required = false) List<SelectedOptionDto> options
    ) {
        return cartService.addItem(cartId, productId, quantity, options);
    }

    @PutMapping("/{cartId}/items/{itemId}")
    public CartDto updateItem(
            @PathVariable String cartId,
            @PathVariable Long itemId,
            @RequestParam(required = false) Integer quantity,
            @RequestBody(required = false) List<SelectedOptionDto> options
    ) {
        return cartService.updateItem(cartId, itemId, quantity, options);
    }

    @DeleteMapping("/{cartId}/items/{itemId}")
    public CartDto removeItem(@PathVariable String cartId, @PathVariable Long itemId) {
        return cartService.removeItem(cartId, itemId);
    }

    @DeleteMapping("/{cartId}/items")
    public CartDto clearCart(@PathVariable String cartId) {
        return cartService.clearCart(cartId);
    }
}
