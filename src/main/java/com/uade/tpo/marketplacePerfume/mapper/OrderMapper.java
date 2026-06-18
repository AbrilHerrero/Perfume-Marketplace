package com.uade.tpo.marketplacePerfume.mapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.Order;
import com.uade.tpo.marketplacePerfume.entity.OrderItem;
import com.uade.tpo.marketplacePerfume.entity.Sample;
import com.uade.tpo.marketplacePerfume.entity.dto.orderDTOs.OrderItemResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.orderDTOs.OrderResponseDTO;

public final class OrderMapper {

    private OrderMapper() {
    }

    public static OrderResponseDTO toResponseDto(Order entity) {
        if (entity == null) {
            return null;
        }

        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(entity.getId());
        if (entity.getBuyer() != null) {
            dto.setBuyerId(entity.getBuyer().getId());
            dto.setBuyerEmail(entity.getBuyer().getEmail());
        }
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setTotal(entity.getTotal());
        dto.setDiscountAmount(entity.getDiscountAmount());
        dto.setCouponCode(entity.getCouponCode());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);
        dto.setItems(toItemResponseDtoList(entity.getOrderItems()));
        return dto;
    }

    public static List<OrderResponseDTO> toResponseDtoList(List<Order> entities) {
        List<OrderResponseDTO> dtos = new ArrayList<>();
        if (entities == null) {
            return dtos;
        }
        for (Order entity : entities) {
            dtos.add(toResponseDto(entity));
        }
        return dtos;
    }

    /**
     * Maps an order for a seller's sales view: keeps only the items sold by this
     * seller and totals just those lines. When the order is entirely the
     * seller's, the coupon discount is attributed to them — the code is exposed
     * and the total is net; in a mixed-seller order the order-wide coupon can't
     * be attributed, so neither it nor the discount is exposed and the total
     * stays the gross of the seller's own lines.
     */
    public static OrderResponseDTO toResponseDtoForSeller(Order entity, Long sellerId) {
        if (entity == null) {
            return null;
        }

        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(entity.getId());
        if (entity.getBuyer() != null) {
            dto.setBuyerId(entity.getBuyer().getId());
            dto.setBuyerEmail(entity.getBuyer().getEmail());
        }
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().name() : null);

        List<OrderItem> sellerItems = new ArrayList<>();
        boolean ownsEveryItem = true;
        if (entity.getOrderItems() != null) {
            for (OrderItem item : entity.getOrderItems()) {
                Sample sample = item.getSample();
                if (sample != null && sample.getSeller() != null
                        && sellerId.equals(sample.getSeller().getId())) {
                    sellerItems.add(item);
                } else {
                    ownsEveryItem = false;
                }
            }
        }

        BigDecimal gross = BigDecimal.ZERO;
        for (OrderItem item : sellerItems) {
            if (item.getUnitPrice() != null) {
                gross = gross.add(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            }
        }

        // The order-wide coupon discount is only attributable to this seller when
        // every line in the order is theirs; otherwise report the gross of their
        // own lines and don't expose another seller's / order-wide coupon.
        BigDecimal discount = entity.getDiscountAmount();
        if (ownsEveryItem && discount != null && discount.signum() > 0) {
            dto.setCouponCode(entity.getCouponCode());
            dto.setDiscountAmount(discount);
            dto.setTotal(gross.subtract(discount).max(BigDecimal.ZERO));
        } else {
            dto.setTotal(gross);
        }
        dto.setItems(toItemResponseDtoList(sellerItems));
        return dto;
    }

    public static List<OrderResponseDTO> toResponseDtoListForSeller(List<Order> entities, Long sellerId) {
        List<OrderResponseDTO> dtos = new ArrayList<>();
        if (entities == null) {
            return dtos;
        }
        for (Order entity : entities) {
            dtos.add(toResponseDtoForSeller(entity, sellerId));
        }
        return dtos;
    }

    public static OrderItemResponseDTO toItemResponseDto(OrderItem entity) {
        if (entity == null) {
            return null;
        }

        OrderItemResponseDTO dto = new OrderItemResponseDTO();
        dto.setId(entity.getId());
        dto.setQuantity(entity.getQuantity());
        dto.setUnitPrice(entity.getUnitPrice());

        if (entity.getUnitPrice() != null) {
            dto.setSubtotal(entity.getUnitPrice().multiply(BigDecimal.valueOf(entity.getQuantity())));
        }

        Sample sample = entity.getSample();
        if (sample != null) {
            dto.setSampleId(sample.getId());
            dto.setSampleDescription(sample.getDescription());
        }
        return dto;
    }

    public static List<OrderItemResponseDTO> toItemResponseDtoList(List<OrderItem> entities) {
        List<OrderItemResponseDTO> dtos = new ArrayList<>();
        if (entities == null) {
            return dtos;
        }
        for (OrderItem entity : entities) {
            dtos.add(toItemResponseDto(entity));
        }
        return dtos;
    }
}
