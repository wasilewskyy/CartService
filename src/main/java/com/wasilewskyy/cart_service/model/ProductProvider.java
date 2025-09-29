package com.wasilewskyy.cart_service.model;

import com.wasilewskyy.cart_service.client.ProductClient;
import com.wasilewskyy.cart_service.exception.ProductNotFoundException;
import com.wasilewskyy.cart_service.model.dto.ProductSummaryDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProductProvider {

    private final ProductClient productClient;

    public ProductSummaryDto getProductById(Long productId) {
        try {
            return productClient.getProductById(productId);
        } catch (Exception e) {
            log.error("Failed to fetch product with id: {}", productId, e);
            throw new ProductNotFoundException("Product not found with id: " + productId);
        }
    }
}
