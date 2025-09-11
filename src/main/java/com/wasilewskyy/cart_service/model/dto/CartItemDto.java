package com.wasilewskyy.cart_service.model.dto;

import com.wasilewskyy.cart_service.model.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDto {
    private Long id;
    private Long productId;
    private String productName;
    private ProductType productType;
    private BigDecimal basePrice;
    private BigDecimal itemPrice;
    private Integer quantity;
    private BigDecimal lineTotal;
    private List<SelectedOptionDto> selectedOptions;
}
