package com.uade.tpo.marketplacePerfume.service.order;

import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.orderDTOs.OrderCreateDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.orderDTOs.OrderResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.orderDTOs.OrderStatusUpdateDTO;

public interface IOrderService {

    List<OrderResponseDTO> getAllOrders();

    List<OrderResponseDTO> getOrdersByBuyer(Long buyerId);

    OrderResponseDTO getOrderById(Long id, User currentUser);

    // Invoked by CartController#/cart/checkout — not exposed directly as a POST on OrderController.
    OrderResponseDTO createOrder(OrderCreateDTO dto, User buyer);

    OrderResponseDTO updateStatus(Long id, OrderStatusUpdateDTO dto);

    OrderResponseDTO cancelOrder(Long id, User currentUser);
}
