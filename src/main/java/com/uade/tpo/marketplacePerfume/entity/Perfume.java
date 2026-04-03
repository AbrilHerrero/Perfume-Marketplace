package com.uade.tpo.marketplacePerfume.entity;

import java.time.Year;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Perfume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brand;

    @Column(name = "release_year")
    private Year releaseYear;

    @Column(name = "image_url", length = 1024)
    private String imageUrl;

    private String gender;

    private String sillage;

    private String confidence;

    @OneToMany (mappedBy = "perfume")
    private List<Sample> samples;


}
