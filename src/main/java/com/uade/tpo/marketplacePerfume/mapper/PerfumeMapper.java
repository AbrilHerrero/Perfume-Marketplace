package com.uade.tpo.marketplacePerfume.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.uade.tpo.marketplacePerfume.adapter.dto.FragellaFragranceResponse;
import com.uade.tpo.marketplacePerfume.adapter.dto.FragellaNoteItem;
import com.uade.tpo.marketplacePerfume.entity.AccordPercentage;
import com.uade.tpo.marketplacePerfume.entity.NoteLayer;
import com.uade.tpo.marketplacePerfume.entity.OccasionRanking;
import com.uade.tpo.marketplacePerfume.entity.Perfume;
import com.uade.tpo.marketplacePerfume.entity.PerfumeNote;
import com.uade.tpo.marketplacePerfume.entity.SeasonRanking;
import com.uade.tpo.marketplacePerfume.entity.dto.AccordPercentageDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.PerfumeDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.PerfumeNoteDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.RankingDTO;

public final class PerfumeMapper {

    private PerfumeMapper() {
    }

    public static PerfumeDTO toDto(Perfume entity) {
        if (entity == null) {
            return null;
        }

        PerfumeDTO dto = new PerfumeDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setBrand(entity.getBrand());
        dto.setLine(entity.getLine());
        dto.setDescription(entity.getDescription());
        dto.setReleaseYear(entity.getReleaseYear());
        dto.setRating(entity.getRating());
        dto.setCountry(entity.getCountry());
        dto.setPrice(entity.getPrice());
        dto.setImageUrl(entity.getImageUrl());
        dto.setGender(entity.getGender());
        dto.setLongevity(entity.getLongevity());
        dto.setSillage(entity.getSillage());
        dto.setPopularity(entity.getPopularity());
        dto.setPriceValue(entity.getPriceValue());
        dto.setConfidence(entity.getConfidence());
        dto.setOilType(entity.getOilType());
        dto.setPurchaseUrl(entity.getPurchaseUrl());
        dto.setGeneralNotes(entity.getGeneralNotes());
        dto.setMainAccords(entity.getMainAccords());
        dto.setImageFallbacks(entity.getImageFallbacks());

        if (entity.getAccordPercentages() != null) {
            dto.setAccordPercentages(entity.getAccordPercentages().stream()
                    .map(a -> AccordPercentageDTO.builder()
                            .accordName(a.getAccordName())
                            .strength(a.getStrength())
                            .build())
                    .collect(Collectors.toList()));
        }

        if (entity.getNotes() != null) {
            dto.setNotes(entity.getNotes().stream()
                    .map(n -> PerfumeNoteDTO.builder()
                            .name(n.getName())
                            .imageUrl(n.getImageUrl())
                            .layer(n.getLayer().name())
                            .build())
                    .collect(Collectors.toList()));
        }

        if (entity.getSeasonRankings() != null) {
            dto.setSeasonRankings(entity.getSeasonRankings().stream()
                    .map(r -> RankingDTO.builder()
                            .name(r.getName())
                            .score(r.getScore())
                            .build())
                    .collect(Collectors.toList()));
        }

        if (entity.getOccasionRankings() != null) {
            dto.setOccasionRankings(entity.getOccasionRankings().stream()
                    .map(r -> RankingDTO.builder()
                            .name(r.getName())
                            .score(r.getScore())
                            .build())
                    .collect(Collectors.toList()));
        }

        return dto;
    }

    public static Perfume toEntity(PerfumeDTO dto) {
        if (dto == null) {
            return null;
        }

        return Perfume.builder()
                .id(dto.getId())
                .name(dto.getName())
                .brand(dto.getBrand())
                .line(dto.getLine())
                .description(dto.getDescription())
                .releaseYear(dto.getReleaseYear())
                .rating(dto.getRating())
                .country(dto.getCountry())
                .price(dto.getPrice())
                .imageUrl(dto.getImageUrl())
                .gender(dto.getGender())
                .longevity(dto.getLongevity())
                .sillage(dto.getSillage())
                .popularity(dto.getPopularity())
                .priceValue(dto.getPriceValue())
                .confidence(dto.getConfidence())
                .oilType(dto.getOilType())
                .purchaseUrl(dto.getPurchaseUrl())
                .generalNotes(dto.getGeneralNotes() != null ? dto.getGeneralNotes() : new ArrayList<>())
                .mainAccords(dto.getMainAccords() != null ? dto.getMainAccords() : new ArrayList<>())
                .imageFallbacks(dto.getImageFallbacks() != null ? dto.getImageFallbacks() : new ArrayList<>())
                .build();
    }

    public static List<PerfumeDTO> toDtoList(List<Perfume> entities) {
        List<PerfumeDTO> dtos = new ArrayList<>();
        if (entities == null) {
            return dtos;
        }

        for (Perfume entity : entities) {
            dtos.add(toDto(entity));
        }
        return dtos;
    }

    public static Perfume fromApiResponse(FragellaFragranceResponse api) {
        if (api == null) {
            return null;
        }

        Perfume perfume = Perfume.builder()
                .name(api.getName())
                .brand(api.getBrand())
                .releaseYear(parseYear(api.getYear()))
                .rating(api.getRating())
                .country(api.getCountry())
                .price(api.getPrice())
                .imageUrl(api.getImageUrl())
                .gender(api.getGender())
                .longevity(api.getLongevity())
                .sillage(api.getSillage())
                .popularity(api.getPopularity())
                .priceValue(api.getPriceValue())
                .confidence(api.getConfidence())
                .oilType(api.getOilType())
                .purchaseUrl(api.getPurchaseUrl())
                .generalNotes(api.getGeneralNotes() != null ? new ArrayList<>(api.getGeneralNotes()) : new ArrayList<>())
                .mainAccords(api.getMainAccords() != null ? new ArrayList<>(api.getMainAccords()) : new ArrayList<>())
                .imageFallbacks(api.getImageFallbacks() != null ? new ArrayList<>(api.getImageFallbacks()) : new ArrayList<>())
                .build();

        if (api.getMainAccordsPercentage() != null) {
            List<AccordPercentage> accords = new ArrayList<>();
            api.getMainAccordsPercentage().forEach((name, strength) -> {
                accords.add(AccordPercentage.builder()
                        .accordName(name)
                        .strength(strength)
                        .perfume(perfume)
                        .build());
            });
            perfume.setAccordPercentages(accords);
        }

        if (api.getNotes() != null) {
            List<PerfumeNote> noteEntities = new ArrayList<>();
            mapNoteLayer(api.getNotes().getTop(), NoteLayer.TOP, perfume, noteEntities);
            mapNoteLayer(api.getNotes().getMiddle(), NoteLayer.MIDDLE, perfume, noteEntities);
            mapNoteLayer(api.getNotes().getBase(), NoteLayer.BASE, perfume, noteEntities);
            perfume.setNotes(noteEntities);
        }

        if (api.getSeasonRanking() != null) {
            List<SeasonRanking> seasons = new ArrayList<>();
            api.getSeasonRanking().forEach(r -> {
                seasons.add(SeasonRanking.builder()
                        .name(r.getName())
                        .score(r.getScore())
                        .perfume(perfume)
                        .build());
            });
            perfume.setSeasonRankings(seasons);
        }

        if (api.getOccasionRanking() != null) {
            List<OccasionRanking> occasions = new ArrayList<>();
            api.getOccasionRanking().forEach(r -> {
                occasions.add(OccasionRanking.builder()
                        .name(r.getName())
                        .score(r.getScore())
                        .perfume(perfume)
                        .build());
            });
            perfume.setOccasionRankings(occasions);
        }

        return perfume;
    }

    private static void mapNoteLayer(List<FragellaNoteItem> items, NoteLayer layer,
                                     Perfume perfume, List<PerfumeNote> target) {
        if (items == null) return;
        for (FragellaNoteItem item : items) {
            target.add(PerfumeNote.builder()
                    .name(item.getName())
                    .imageUrl(item.getImageUrl())
                    .layer(layer)
                    .perfume(perfume)
                    .build());
        }
    }

    private static int parseYear(String year) {
        try {
            return year != null && !year.isBlank() ? Integer.parseInt(year.trim()) : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
