package com.wasilewskyy.cart_service.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    private String id;

    private String currency = "PLN";
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CartItem> items = new ArrayList<>();


    public BigDecimal calculateTotal() {
        return items.stream()
                .map(CartItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void addItem(CartItem item) {
        item.setCart(this);
        items.add(item);
    }

    public void removeItem(CartItem item) {
        items.remove(item);
        item.setCart(null);
    }
}

