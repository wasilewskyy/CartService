package com.wasilewskyy.cart_service.model.entity;

import com.wasilewskyy.cart_service.mapper.CartMapper;
import com.wasilewskyy.cart_service.model.dto.ProductSummaryDto;
import com.wasilewskyy.cart_service.model.dto.SelectedOptionDto;
import com.wasilewskyy.cart_service.model.enums.ProductType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    private Long productId;
    private String productName;

    @Enumerated(EnumType.STRING)
    private ProductType productType;

    private BigDecimal basePrice;
    private Integer quantity = 1;

    @ElementCollection
    @CollectionTable(name = "cart_item_options", joinColumns = @JoinColumn(name = "cart_item_id"))
    private List<SelectedOption> selectedOptions = new ArrayList<>();

    public BigDecimal getOptionsPrice() {
        return selectedOptions.stream()
                .map(SelectedOption::getAdditionalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getItemPrice() {
        return basePrice.add(getOptionsPrice());
    }

    public BigDecimal getLineTotal() {
        return getItemPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public void incrementQuantity(int amount) {
        this.quantity += amount;
    }

    public void updateOptions(List<SelectedOption> newOptions) {
        this.selectedOptions.clear();
        this.selectedOptions.addAll(newOptions);
    }
}