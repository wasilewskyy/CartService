package com.wasilewskyy.cart_service.client;

import com.wasilewskyy.cart_service.config.FeignConfig;
import com.wasilewskyy.cart_service.model.dto.ProductSummaryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", url = "${spring.cloud.openfeign.client.config.productClient.url}", configuration = FeignConfig.class)
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    ProductSummaryDto getProductById(@PathVariable("id") Long id);
}
