package com.uade.tpo.marketplacePerfume.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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

    private String numStreet;

    private String city;

    private String state;

    private String postalCode;

    private String country;

    private String aparment;

    @OneToOne
    @JoinColumn(name = "buyer_id", referencedColumnName = "id", unique = true)
    private User buyer;

}
