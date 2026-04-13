package com.uade.tpo.marketplacePerfume.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplacePerfume.entity.Shipment;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
}
