package com.uade.tpo.marketplacePerfume.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplacePerfume.entity.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findAllByBuyer_IdAndActiveTrueOrderByIdAsc(Long buyerId);

    Optional<Address> findByIdAndBuyer_Id(Long addressId, Long buyerId);

    Optional<Address> findByIdAndBuyer_IdAndActiveTrue(Long addressId, Long buyerId);
}
