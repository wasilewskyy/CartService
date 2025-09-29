package com.wasilewskyy.cart_service.model.dto;

import com.wasilewskyy.cart_service.model.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSummaryDto {

    private Long id;
    private String name;
    private BigDecimal price;
    private ProductType type;
}