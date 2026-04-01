package com.uade.tpo.marketplacePerfume.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class CartItem {
    // id, cart_id, sample_id, amount, added_at
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private int amount;
    private LocalDateTime addedAt;
    
    @ManyToOne 
    @JoinColumn(name = "cartId", referencedColumnName= "id")
    private Cart cart;

    @ManyToOne
    @JoinColumn(name="sampleId", referencedColumnName = "id")
    private Sample sample;
    
}
