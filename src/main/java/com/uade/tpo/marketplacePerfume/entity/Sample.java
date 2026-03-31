package com.uade.tpo.marketplacePerfume.entity;

import java.time.LocalDate;

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
public class Sample {
   
    //public Sample(){}
    //public Sample (int volume,double price, String description, String image_url, int stock )
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int volume_ml;
    private double price;
    private String description;
    private String image_url;
    private LocalDate created_at;
    private int stock;

    @ManyToOne
    @JoinColumn(name="perfume_id", referencedColumnName = "id")
    private Perfume perfume;
    
    @ManyToOne
    @JoinColumn(name="seller_id", referencedColumnName = "id")
    private User seller;



}
