package com.wasilewskyy.cart_service.mapper;

import com.wasilewskyy.cart_service.model.dto.CartDto;
import com.wasilewskyy.cart_service.model.dto.CartItemDto;
import com.wasilewskyy.cart_service.model.dto.SelectedOptionDto;
import com.wasilewskyy.cart_service.model.entity.Cart;
import com.wasilewskyy.cart_service.model.entity.CartItem;
import com.wasilewskyy.cart_service.model.entity.SelectedOption;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {

    CartDto toDto(Cart cart);

    CartItemDto toDto(CartItem item);

    SelectedOptionDto toDto(SelectedOption option);

    SelectedOption toEntity(SelectedOptionDto dto);
}
