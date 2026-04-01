package com.uade.tpo.marketplacePerfume.entity;

import java.time.LocalDateTime;
import java.util.List;
import java.math.BigDecimal;
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
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Sample {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int volumeMl;
    private BigDecimal price;
    private String description;
    private String imageUrl;
    private LocalDateTime createdAt;
    private int stock;

    @ManyToOne
    @JoinColumn(name="perfume_id", referencedColumnName = "id")
    private Perfume perfume;
    
    @ManyToOne
    @JoinColumn(name="seller_id", referencedColumnName = "id")
    private User seller;

    @OneToMany (mappedBy = "sample")
    private List<Review> reviews;



}
