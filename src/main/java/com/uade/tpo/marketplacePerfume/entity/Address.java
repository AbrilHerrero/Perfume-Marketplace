package com.uade.tpo.marketplacePerfume.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;

    private String streetNumber;

    private String city;

    private String state;

    private String postalCode;

    private String country;

    private String apartment;

    @Column(nullable = false)
    private boolean active;

    @ManyToOne(optional = false)
    @JoinColumn(name = "buyer_id", referencedColumnName = "id", unique = false)
    private User buyer;

    @OneToMany(mappedBy = "address")
    private List<Shipment> shipments;

}
