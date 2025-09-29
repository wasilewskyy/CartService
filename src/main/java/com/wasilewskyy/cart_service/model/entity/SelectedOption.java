package com.wasilewskyy.cart_service.model.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectedOption {
    private String type;
    private String value;
    private String displayName;
    private BigDecimal additionalPrice = BigDecimal.ZERO;
    private Long productId;
    private Long configurationId;
}
