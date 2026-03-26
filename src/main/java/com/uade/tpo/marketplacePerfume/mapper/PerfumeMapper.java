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
import com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs.AccordPercentageDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs.PerfumeNoteDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs.RankingDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs.PerfumeCreateDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs.PerfumeModifyDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs.PerfumeResponseDTO;

public final class PerfumeMapper {

    private PerfumeMapper() {
    }

    public static PerfumeResponseDTO toResponseDto(Perfume entity) {
        if (entity == null) {
            return null;
        }

        PerfumeResponseDTO dto = new PerfumeResponseDTO();
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

    public static Perfume toEntityFromCreate(PerfumeCreateDTO dto) {
        if (dto == null) {
            return null;
        }

        return Perfume.builder()
                .name(dto.getName())
                .brand(dto.getBrand())
                .line(dto.getLine())
                .description(dto.getDescription())
                .releaseYear(dto.getReleaseYear() != null ? dto.getReleaseYear() : 0)
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

    public static void applyModify(PerfumeModifyDTO dto, Perfume existing) {
        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getBrand() != null) existing.setBrand(dto.getBrand());
        if (dto.getLine() != null) existing.setLine(dto.getLine());
        if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if (dto.getReleaseYear() != null) existing.setReleaseYear(dto.getReleaseYear());
        if (dto.getRating() != null) existing.setRating(dto.getRating());
        if (dto.getCountry() != null) existing.setCountry(dto.getCountry());
        if (dto.getPrice() != null) existing.setPrice(dto.getPrice());
        if (dto.getImageUrl() != null) existing.setImageUrl(dto.getImageUrl());
        if (dto.getGender() != null) existing.setGender(dto.getGender());
        if (dto.getLongevity() != null) existing.setLongevity(dto.getLongevity());
        if (dto.getSillage() != null) existing.setSillage(dto.getSillage());
        if (dto.getPopularity() != null) existing.setPopularity(dto.getPopularity());
        if (dto.getPriceValue() != null) existing.setPriceValue(dto.getPriceValue());
        if (dto.getConfidence() != null) existing.setConfidence(dto.getConfidence());
        if (dto.getOilType() != null) existing.setOilType(dto.getOilType());
        if (dto.getPurchaseUrl() != null) existing.setPurchaseUrl(dto.getPurchaseUrl());
        if (dto.getGeneralNotes() != null) existing.setGeneralNotes(dto.getGeneralNotes());
        if (dto.getMainAccords() != null) existing.setMainAccords(dto.getMainAccords());
        if (dto.getImageFallbacks() != null) existing.setImageFallbacks(dto.getImageFallbacks());
    }

    public static List<PerfumeResponseDTO> toResponseDtoList(List<Perfume> entities) {
        List<PerfumeResponseDTO> dtos = new ArrayList<>();
        if (entities == null) {
            return dtos;
        }

        for (Perfume entity : entities) {
            dtos.add(toResponseDto(entity));
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
