package com.uade.tpo.marketplacePerfume.entity.dto.cartDTOs;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.dto.cartItemDTOs.CartItemResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.userDTOs.UserResponseDTO;

import lombok.Data;

@Data
public class CartResponseDTO {

    private Long id;
    private UserResponseDTO buyer;
    private List<CartItemResponseDTO> items;
    private BigDecimal totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
