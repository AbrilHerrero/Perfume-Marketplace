package com.uade.tpo.marketplacePerfume.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Perfume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brand;

    @Column(name = "line_name")
    private String line;

    private String description;

    @Column(name = "release_year")
    private int releaseYear;

    private String rating;

    private String country;

    private String price;

    @Column(name = "image_url", length = 1024)
    private String imageUrl;

    private String gender;

    private String longevity;

    private String sillage;

    private String popularity;

    @Column(name = "price_value")
    private String priceValue;

    private String confidence;

    @Column(name = "oil_type")
    private String oilType;

    @Column(name = "purchase_url", length = 1024)
    private String purchaseUrl;

    @ElementCollection
    @CollectionTable(name = "perfume_general_notes", joinColumns = @JoinColumn(name = "perfume_id"))
    @Column(name = "note")
    @Builder.Default
    private List<String> generalNotes = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "perfume_main_accords", joinColumns = @JoinColumn(name = "perfume_id"))
    @Column(name = "accord")
    @Builder.Default
    private List<String> mainAccords = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "perfume_image_fallbacks", joinColumns = @JoinColumn(name = "perfume_id"))
    @Column(name = "url", length = 1024)
    @Builder.Default
    private List<String> imageFallbacks = new ArrayList<>();

    @OneToMany(mappedBy = "perfume", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AccordPercentage> accordPercentages = new ArrayList<>();

    @OneToMany(mappedBy = "perfume", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PerfumeNote> notes = new ArrayList<>();

    @OneToMany(mappedBy = "perfume", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SeasonRanking> seasonRankings = new ArrayList<>();

    @OneToMany(mappedBy = "perfume", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OccasionRanking> occasionRankings = new ArrayList<>();
}
