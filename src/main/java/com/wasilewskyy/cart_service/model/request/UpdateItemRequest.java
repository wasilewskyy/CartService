package com.wasilewskyy.cart_service.model.request;

import com.wasilewskyy.cart_service.model.dto.SelectedOptionDto;
import lombok.Data;

import java.util.List;

@Data
public class UpdateItemRequest {
    private Integer quantity;
    private List<SelectedOptionDto> selectedOptions;
}
