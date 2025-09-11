package com.wasilewskyy.cart_service.service;

import com.wasilewskyy.cart_service.client.ProductClient;
import com.wasilewskyy.cart_service.exception.CartItemNotFoundException;
import com.wasilewskyy.cart_service.exception.CartNotFoundException;
import com.wasilewskyy.cart_service.exception.ProductNotFoundException;
import com.wasilewskyy.cart_service.mapper.CartMapper;
import com.wasilewskyy.cart_service.model.dto.CartDto;
import com.wasilewskyy.cart_service.model.dto.ProductSummaryDto;
import com.wasilewskyy.cart_service.model.dto.SelectedOptionDto;
import com.wasilewskyy.cart_service.model.entity.Cart;
import com.wasilewskyy.cart_service.model.entity.CartItem;
import com.wasilewskyy.cart_service.model.entity.SelectedOption;
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
    private final ProductClient productClient;
    private final CartMapper cartMapper;

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
        ProductSummaryDto product = getProduct(productId);

        if (quantity == null || quantity <= 0) {
            quantity = 1;
        }

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProductId().equals(productId) &&
                        hasSameOptions(item.getSelectedOptions(), options))
                .findFirst();

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        } else {
            CartItem newItem = createCartItem(product, quantity, options);
            cart.addItem(newItem);
        }

        Cart savedCart = cartRepository.save(cart);
        log.info("Added item to cart {}: product {} with quantity {}", cartId, productId, quantity);
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
            List<SelectedOption> selectedOptions = options.stream()
                    .map(cartMapper::toEntity)
                    .toList();
            item.setSelectedOptions(selectedOptions);
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
        cart.getItems().clear();
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

    private ProductSummaryDto getProduct(Long productId) {
        try {
            return productClient.getProductById(productId);
        } catch (Exception e) {
            log.error("Failed to fetch product with id: {}", productId, e);
            throw new ProductNotFoundException("Product not found with id: " + productId);
        }
    }

    private CartItem createCartItem(ProductSummaryDto product, Integer quantity, List<SelectedOptionDto> options) {
        CartItem item = new CartItem();
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setProductType(product.getType());
        item.setBasePrice(product.getPrice());
        item.setQuantity(quantity);

        if (options != null) {
            List<SelectedOption> selectedOptions = options.stream()
                    .map(cartMapper::toEntity)
                    .toList();
            item.setSelectedOptions(selectedOptions);
        }

        return item;
    }

    private boolean hasSameOptions(List<SelectedOption> existing, List<SelectedOptionDto> newOptions) {
        if (existing.size() != (newOptions != null ? newOptions.size() : 0)) {
            return false;
        }

        if (newOptions == null) {
            return existing.isEmpty();
        }

        return existing.stream()
                .allMatch(opt -> newOptions.stream()
                        .anyMatch(newOpt -> opt.getType().equals(newOpt.getType()) &&
                                opt.getValue().equals(newOpt.getValue())));
    }
}