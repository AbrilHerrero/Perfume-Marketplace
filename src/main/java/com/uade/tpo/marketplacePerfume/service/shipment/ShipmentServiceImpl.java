package com.uade.tpo.marketplacePerfume.service.shipment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplacePerfume.entity.Address;
import com.uade.tpo.marketplacePerfume.entity.Order;
import com.uade.tpo.marketplacePerfume.entity.Role;
import com.uade.tpo.marketplacePerfume.entity.Shipment;
import com.uade.tpo.marketplacePerfume.entity.ShipmentStatus;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.shipment.CreateShipmentRequest;
import com.uade.tpo.marketplacePerfume.entity.dto.shipment.ShipmentResponse;
import com.uade.tpo.marketplacePerfume.entity.dto.shipment.UpdateShipmentStatusRequest;
import com.uade.tpo.marketplacePerfume.entity.dto.shipment.UpdateShipmentTrackingRequest;
import com.uade.tpo.marketplacePerfume.exceptions.address.AddressNotFoundException;
import com.uade.tpo.marketplacePerfume.exceptions.order.OrderNotFoundException;
import com.uade.tpo.marketplacePerfume.exceptions.shipment.ShipmentAlreadyExistsException;
import com.uade.tpo.marketplacePerfume.exceptions.shipment.ShipmentForbiddenException;
import com.uade.tpo.marketplacePerfume.exceptions.shipment.ShipmentNotFoundException;
import com.uade.tpo.marketplacePerfume.mapper.ShipmentMapper;
import com.uade.tpo.marketplacePerfume.repository.AddressRepository;
import com.uade.tpo.marketplacePerfume.repository.OrderRepository;
import com.uade.tpo.marketplacePerfume.repository.ShipmentRepository;

@Service
public class ShipmentServiceImpl implements IShipmentService {

    @Autowired
    private ShipmentRepository shipmentRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private AddressRepository addressRepository;

    @Override
    @Transactional(readOnly = true)
    public ShipmentResponse getById(Long id, User currentUser) {
        Shipment shipment = shipmentRepository.findFetchedById(id)
                .orElseThrow(ShipmentNotFoundException::new);
        assertCanViewShipment(currentUser, shipment);
        return ShipmentMapper.toResponse(shipment);
    }

    @Override
    @Transactional(readOnly = true)
    public ShipmentResponse getByOrderId(Long orderId, User currentUser) {
        Shipment shipment = shipmentRepository.findFetchedByOrderId(orderId)
                .orElseThrow(ShipmentNotFoundException::new);
        assertCanViewShipment(currentUser, shipment);
        return ShipmentMapper.toResponse(shipment);
    }

    @Override
    @Transactional
    public ShipmentResponse create(CreateShipmentRequest request, User currentUser) {
        Order order = orderRepository.findByIdWithBuyerAndItems(request.getOrderId())
                .orElseThrow(OrderNotFoundException::new);
        assertCanMutateShipment(currentUser, order);
        if (shipmentRepository.existsByOrder_Id(order.getId())) {
            throw new ShipmentAlreadyExistsException();
        }
        Address address = addressRepository.findByIdAndBuyer_IdAndActiveTrue(request.getAddressId(),
                order.getBuyer().getId())
                .orElseThrow(AddressNotFoundException::new);
        ShipmentStatus status = normalizeStatusOrDefault(request.getStatus());
        Shipment entity = ShipmentMapper.toNewEntity(order, address, status, request.getTrackingNumber());
        Shipment saved = shipmentRepository.save(entity);
        return ShipmentMapper.toResponse(shipmentRepository.findFetchedById(saved.getId())
                .orElse(saved));
    }

    @Override
    @Transactional
    public ShipmentResponse updateStatus(Long id, UpdateShipmentStatusRequest request, User currentUser) {
        Shipment shipment = shipmentRepository.findFetchedById(id)
                .orElseThrow(ShipmentNotFoundException::new);
        assertCanMutateShipment(currentUser, shipment.getOrder());
        shipment.setStatus(request.getStatus());
        shipmentRepository.save(shipment);
        return ShipmentMapper.toResponse(shipment);
    }

    @Override
    @Transactional
    public ShipmentResponse updateTracking(Long id, UpdateShipmentTrackingRequest request, User currentUser) {
        Shipment shipment = shipmentRepository.findFetchedById(id)
                .orElseThrow(ShipmentNotFoundException::new);
        assertCanMutateShipment(currentUser, shipment.getOrder());
        shipment.setTrackingNumber(request.getTrackingNumber().trim());
        shipmentRepository.save(shipment);
        return ShipmentMapper.toResponse(shipment);
    }

    private void assertCanViewShipment(User user, Shipment shipment) {
        if (user.getRole() == Role.ADMIN) {
            return;
        }
        if (user.getRole() == Role.BUYER
                && shipment.getOrder().getBuyer().getId().equals(user.getId())) {
            return;
        }
        if (user.getRole() == Role.SELLER && sellerOwnsOrder(shipment.getOrder(), user.getId())) {
            return;
        }
        throw new ShipmentForbiddenException();
    }

    private void assertCanMutateShipment(User user, Order order) {
        if (user.getRole() == Role.ADMIN) {
            return;
        }
        if (user.getRole() == Role.SELLER && sellerOwnsOrder(order, user.getId())) {
            return;
        }
        throw new ShipmentForbiddenException();
    }

    private boolean sellerOwnsOrder(Order order, Long sellerId) {
        if (order.getOrderItems() == null || order.getOrderItems().isEmpty()) {
            return false;
        }
        return order.getOrderItems().stream()
                .allMatch(oi -> oi.getSample() != null
                        && oi.getSample().getSeller() != null
                        && oi.getSample().getSeller().getId().equals(sellerId));
    }

    private ShipmentStatus normalizeStatusOrDefault(ShipmentStatus status) {
        return status != null ? status : ShipmentStatus.PENDING;
    }
}
