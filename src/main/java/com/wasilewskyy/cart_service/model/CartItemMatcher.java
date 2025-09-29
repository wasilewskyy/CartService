package com.wasilewskyy.cart_service.model;

import com.wasilewskyy.cart_service.model.dto.SelectedOptionDto;
import com.wasilewskyy.cart_service.model.entity.Cart;
import com.wasilewskyy.cart_service.model.entity.CartItem;
import com.wasilewskyy.cart_service.model.entity.SelectedOption;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class CartItemMatcher {

    public Optional<CartItem> findMatchingItem(Cart cart, Long productId, List<SelectedOptionDto> options) {
        return cart.getItems().stream()
                .filter(item -> matchesProduct(item, productId) && matchesOptions(item, options))
                .findFirst();
    }

    private boolean matchesProduct(CartItem item, Long productId) {
        return item.getProductId().equals(productId);
    }

    private boolean matchesOptions(CartItem item, List<SelectedOptionDto> newOptions) {
        List<SelectedOption> existingOptions = item.getSelectedOptions();

        if (existingOptions.size() != getOptionsSize(newOptions)) {
            return false;
        }

        if (newOptions == null) {
            return existingOptions.isEmpty();
        }

        return existingOptions.stream()
                .allMatch(existing -> containsMatchingOption(newOptions, existing));
    }

    private int getOptionsSize(List<SelectedOptionDto> options) {
        return options != null ? options.size() : 0;
    }

    private boolean containsMatchingOption(List<SelectedOptionDto> options, SelectedOption existing) {
        return options.stream()
                .anyMatch(newOpt ->
                        existing.getType().equals(newOpt.getType()) &&
                                existing.getValue().equals(newOpt.getValue())
                );
    }
}
