package com.wasilewskyy.cart_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoneyDto {

    private BigDecimal amount;
    private String currency;
}
