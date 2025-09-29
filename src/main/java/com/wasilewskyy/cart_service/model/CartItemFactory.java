package com.wasilewskyy.cart_service.model;

import com.wasilewskyy.cart_service.mapper.CartMapper;
import com.wasilewskyy.cart_service.model.dto.ProductSummaryDto;
import com.wasilewskyy.cart_service.model.dto.SelectedOptionDto;
import com.wasilewskyy.cart_service.model.entity.CartItem;
import com.wasilewskyy.cart_service.model.entity.SelectedOption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CartItemFactory {

    private final CartMapper cartMapper;

    public CartItem createCartItem(ProductSummaryDto product, Integer quantity, List<SelectedOptionDto> options) {
        CartItem item = new CartItem();
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setProductType(product.getType());
        item.setBasePrice(product.getPrice());
        item.setQuantity(quantity);

        if (options != null && !options.isEmpty()) {
            item.setSelectedOptions(mapToSelectedOptions(options));
        }

        return item;
    }

    public List<SelectedOption> mapToSelectedOptions(List<SelectedOptionDto> options) {
        if (options == null || options.isEmpty()) {
            return Collections.emptyList();
        }

        return options.stream()
                .map(cartMapper::toEntity)
                .toList();
    }
}
