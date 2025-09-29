package com.wasilewskyy.cart_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectedOptionDto {
    private String type;
    private String value;
    private String displayName;
    private BigDecimal additionalPrice;
    private Long productId;
    private Long configurationId;
}
