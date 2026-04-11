package com.uade.tpo.marketplacePerfume.mapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.Cart;
import com.uade.tpo.marketplacePerfume.entity.CartItem;
import com.uade.tpo.marketplacePerfume.entity.Sample;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.cartDTOs.CartResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.cartDTOs.CartStockCheckResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.cartDTOs.CartStockLineResultDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.cartItemDTOs.CartItemResponseDTO;

public final class CartMapper {

    private CartMapper() {
    }

    public static CartItemResponseDTO toCartItemResponseDto(CartItem item) {
        if (item == null) {
            return null;
        }
        CartItemResponseDTO dto = new CartItemResponseDTO();
        dto.setId(item.getId());
        dto.setQuantity(item.getQuantity());
        dto.setAddedAt(item.getAddedAt());
        dto.setSample(SampleMapper.toResponseDto(item.getSample()));
        return dto;
    }

    public static List<CartItemResponseDTO> toCartItemResponseDtoList(List<CartItem> items) {
        if (items == null || items.isEmpty()) {
            return Collections.emptyList();
        }
        List<CartItemResponseDTO> out = new ArrayList<>(items.size());
        for (CartItem item : items) {
            out.add(toCartItemResponseDto(item));
        }
        return out;
    }

    public static CartResponseDTO toCartResponseDto(Cart cart, List<CartItem> lineItems) {
        CartResponseDTO dto = new CartResponseDTO();
        if (cart != null) {
            dto.setId(cart.getId());
            dto.setCreatedAt(cart.getCreatedAt());
            dto.setUpdatedAt(cart.getUpdatedAt());
            dto.setBuyer(UserMapper.toResponseDto(cart.getBuyer()));
        }
        dto.setItems(toCartItemResponseDtoList(lineItems));
        dto.setTotalPrice(computeTotal(lineItems));
        return dto;
    }

    public static CartResponseDTO toEmptyCartResponse(User user) {
        CartResponseDTO dto = new CartResponseDTO();
        dto.setBuyer(UserMapper.toResponseDto(user));
        dto.setItems(Collections.emptyList());
        dto.setTotalPrice(BigDecimal.ZERO);
        return dto;
    }

    public static CartStockCheckResponseDTO toCartStockCheckResponseDto(List<CartItem> lineItems) {
        CartStockCheckResponseDTO dto = new CartStockCheckResponseDTO();
        if (lineItems == null || lineItems.isEmpty()) {
            dto.setAllSufficient(true);
            dto.setLines(Collections.emptyList());
            return dto;
        }
        List<CartStockLineResultDTO> lines = new ArrayList<>(lineItems.size());
        boolean allOk = true;
        for (CartItem ci : lineItems) {
            Sample s = ci.getSample();
            int available = s != null ? s.getStock() : 0;
            int requested = ci.getQuantity();
            boolean sufficient = available >= requested;
            if (!sufficient) {
                allOk = false;
            }
            CartStockLineResultDTO line = new CartStockLineResultDTO();
            line.setCartItemId(ci.getId());
            line.setSampleId(s != null ? s.getId() : null);
            line.setRequestedQuantity(requested);
            line.setAvailableStock(available);
            line.setSufficient(sufficient);
            lines.add(line);
        }
        dto.setAllSufficient(allOk);
        dto.setLines(lines);
        return dto;
    }

    private static BigDecimal computeTotal(List<CartItem> lineItems) {
        if (lineItems == null || lineItems.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem ci : lineItems) {
            Sample s = ci.getSample();
            if (s == null || s.getPrice() == null) {
                continue;
            }
            total = total.add(s.getPrice().multiply(BigDecimal.valueOf(ci.getQuantity())));
        }
        return total;
    }
}
