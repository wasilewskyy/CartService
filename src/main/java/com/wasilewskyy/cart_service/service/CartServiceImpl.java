package com.wasilewskyy.cart_service.service;

import com.wasilewskyy.cart_service.exception.CartItemNotFoundException;
import com.wasilewskyy.cart_service.exception.CartNotFoundException;
import com.wasilewskyy.cart_service.mapper.CartMapper;
import com.wasilewskyy.cart_service.model.CartItemFactory;
import com.wasilewskyy.cart_service.model.CartItemMatcher;
import com.wasilewskyy.cart_service.model.ProductProvider;
import com.wasilewskyy.cart_service.model.dto.CartDto;
import com.wasilewskyy.cart_service.model.dto.SelectedOptionDto;
import com.wasilewskyy.cart_service.model.entity.Cart;
import com.wasilewskyy.cart_service.model.entity.CartItem;
import com.wasilewskyy.cart_service.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductProvider productProvider;
    private final CartMapper cartMapper;
    private final CartItemMatcher cartItemMatcher;
    private final CartItemFactory cartItemFactory;

    @Override
    public CartDto createCart() {
        Cart cart = new Cart();
        Cart savedCart = cartRepository.save(cart);
        log.info("Created new cart with id: {}", savedCart.getId());
        return cartMapper.toDto(savedCart);
    }

    @Override
    @Transactional
    public CartDto getCart(String cartId) {
        Cart cart = findCartById(cartId);
        return cartMapper.toDto(cart);
    }

    @Override
    public CartDto addItem(String cartId, Long productId, Integer quantity, List<SelectedOptionDto> options) {
        Cart cart = findCartById(cartId);
        var product = productProvider.getProductById(productId);

        int validatedQuantity = validateQuantity(quantity);

        Optional<CartItem> existingItem = cartItemMatcher.findMatchingItem(cart, productId, options);

        if (existingItem.isPresent()) {
            existingItem.get().incrementQuantity(validatedQuantity);
        } else {
            CartItem newItem = cartItemFactory.createCartItem(product, validatedQuantity, options);
            cart.addItem(newItem);
        }

        Cart savedCart = cartRepository.save(cart);
        log.info("Added item to cart {}: product {} with quantity {}", cartId, productId, validatedQuantity);
        return cartMapper.toDto(savedCart);
    }

    @Override
    public CartDto updateItem(String cartId, Long itemId, Integer quantity, List<SelectedOptionDto> options) {
        Cart cart = findCartById(cartId);
        CartItem item = findCartItem(cart, itemId);

        if (quantity != null && quantity > 0) {
            item.setQuantity(quantity);
        }

        if (options != null) {
            item.updateOptions(cartItemFactory.mapToSelectedOptions(options));
        }

        Cart savedCart = cartRepository.save(cart);
        log.info("Updated item {} in cart {}", itemId, cartId);
        return cartMapper.toDto(savedCart);
    }

    @Override
    public CartDto removeItem(String cartId, Long itemId) {
        Cart cart = findCartById(cartId);
        CartItem item = findCartItem(cart, itemId);

        cart.removeItem(item);
        Cart savedCart = cartRepository.save(cart);
        log.info("Removed item {} from cart {}", itemId, cartId);
        return cartMapper.toDto(savedCart);
    }

    @Override
    public CartDto clearCart(String cartId) {
        Cart cart = findCartById(cartId);
        cart.clearItems();
        Cart savedCart = cartRepository.save(cart);
        log.info("Cleared cart {}", cartId);
        return cartMapper.toDto(savedCart);
    }

    private Cart findCartById(String cartId) {
        return cartRepository.findByIdWithItems(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + cartId));
    }

    private CartItem findCartItem(Cart cart, Long itemId) {
        return cart.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found with id: " + itemId));
    }

    private int validateQuantity(Integer quantity) {
        return (quantity == null || quantity <= 0) ? 1 : quantity;
    }
}