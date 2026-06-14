package com.uade.tpo.marketplacePerfume.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.uade.tpo.marketplacePerfume.adapter.dto.FragellaFragranceResponse;
import com.uade.tpo.marketplacePerfume.adapter.dto.FragellaFragranceResponse.NoteEntry;
import com.uade.tpo.marketplacePerfume.adapter.dto.FragellaFragranceResponse.NotesDetail;
import com.uade.tpo.marketplacePerfume.adapter.dto.FragellaFragranceResponse.RankingEntry;
import com.uade.tpo.marketplacePerfume.entity.OccasionProfile;
import com.uade.tpo.marketplacePerfume.entity.Perfume;
import com.uade.tpo.marketplacePerfume.entity.PerfumeNotes;
import com.uade.tpo.marketplacePerfume.entity.SeasonProfile;
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
        dto.setImageUrl(entity.getImageUrl());
        dto.setGender(entity.getGender());
        dto.setSillage(entity.getSillage());
        dto.setConfidence(entity.getConfidence());
        dto.setNotes(entity.getNotes());
        dto.setAccords(entity.getAccords());
        dto.setSeason(entity.getSeason());
        dto.setOccasion(entity.getOccasion());

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
                .imageUrl(dto.getImageUrl())
                .gender(dto.getGender())
                .sillage(dto.getSillage())
                .confidence(dto.getConfidence())
                .notes(dto.getNotes())
                .accords(dto.getAccords())
                .season(dto.getSeason())
                .occasion(dto.getOccasion())
                .build();
    }

    public static void applyModify(PerfumeModifyDTO dto, Perfume existing) {
        if (dto.getName() != null) existing.setName(dto.getName());
        if (dto.getBrand() != null) existing.setBrand(dto.getBrand());
        if (dto.getLine() != null) existing.setLine(dto.getLine());
        if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if (dto.getReleaseYear() != null) existing.setReleaseYear(dto.getReleaseYear());
        if (dto.getImageUrl() != null) existing.setImageUrl(dto.getImageUrl());
        if (dto.getGender() != null) existing.setGender(dto.getGender());
        if (dto.getSillage() != null) existing.setSillage(dto.getSillage());
        if (dto.getConfidence() != null) existing.setConfidence(dto.getConfidence());
        if (dto.getNotes() != null) existing.setNotes(dto.getNotes());
        if (dto.getAccords() != null) existing.setAccords(dto.getAccords());
        if (dto.getSeason() != null) existing.setSeason(dto.getSeason());
        if (dto.getOccasion() != null) existing.setOccasion(dto.getOccasion());
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

        return Perfume.builder()
                .name(api.getName())
                .brand(api.getBrand())
                .line(extractLine(api.getBrand(), api.getName()))
                .description(buildDescription(api))
                .releaseYear(parseYear(api.getYear()))
                .imageUrl(api.getImageUrl())
                .gender(api.getGender())
                .sillage(api.getSillage())
                .confidence(api.getConfidence())
                .notes(mapNotes(api.getNotes()))
                .accords(formatAccords(api.getMainAccords()))
                .season(mapSeasonRanking(api.getSeasonRanking()))
                .occasion(mapOccasionRanking(api.getOccasionRanking()))
                .build();
    }

    private static PerfumeNotes mapNotes(NotesDetail notes) {
        if (notes == null) {
            return null;
        }

        return PerfumeNotes.builder()
                .top(extractNoteNames(notes.getTop()))
                .middle(extractNoteNames(notes.getMiddle()))
                .base(extractNoteNames(notes.getBase()))
                .build();
    }

    private static List<String> extractNoteNames(List<NoteEntry> entries) {
        if (entries == null || entries.isEmpty()) {
            return List.of();
        }

        return entries.stream()
                .map(NoteEntry::getName)
                .filter(name -> name != null && !name.isBlank())
                .collect(Collectors.toList());
    }

    private static List<String> formatAccords(List<String> accords) {
        if (accords == null || accords.isEmpty()) {
            return List.of();
        }

        return accords.stream()
                .map(accord -> {
                    if (accord == null || accord.isBlank()) {
                        return null;
                    }
                    String trimmed = accord.trim();
                    return Character.toUpperCase(trimmed.charAt(0)) + trimmed.substring(1);
                })
                .filter(accord -> accord != null)
                .collect(Collectors.toList());
    }

    private static SeasonProfile mapSeasonRanking(List<RankingEntry> ranking) {
        Map<String, Double> normalized = normalizeRanking(ranking);
        return SeasonProfile.builder()
                .spring(normalized.getOrDefault("spring", 0.5))
                .summer(normalized.getOrDefault("summer", 0.5))
                .autumn(normalized.getOrDefault("autumn",
                        normalized.getOrDefault("fall", 0.5)))
                .winter(normalized.getOrDefault("winter", 0.5))
                .build();
    }

    private static OccasionProfile mapOccasionRanking(List<RankingEntry> ranking) {
        Map<String, Double> normalized = normalizeRanking(ranking);
        return OccasionProfile.builder()
                .daily(firstPresent(normalized, "casual", "daily"))
                .office(firstPresent(normalized, "professional", "office"))
                .evening(firstPresent(normalized, "night out", "evening", "night"))
                .special(firstPresent(normalized, "special", "formal", "black tie"))
                .build();
    }

    private static double firstPresent(Map<String, Double> values, String... keys) {
        for (String key : keys) {
            if (values.containsKey(key)) {
                return values.get(key);
            }
        }
        return 0.5;
    }

    private static Map<String, Double> normalizeRanking(List<RankingEntry> ranking) {
        Map<String, Double> result = new HashMap<>();
        if (ranking == null || ranking.isEmpty()) {
            return result;
        }

        double maxScore = ranking.stream()
                .mapToDouble(RankingEntry::getScore)
                .max()
                .orElse(1.0);

        if (maxScore <= 0) {
            maxScore = 1.0;
        }

        for (RankingEntry entry : ranking) {
            if (entry.getName() == null) {
                continue;
            }
            String key = entry.getName().trim().toLowerCase(Locale.ROOT);
            result.put(key, roundToTwoDecimals(entry.getScore() / maxScore));
        }

        return result;
    }

    public static String extractLine(String brand, String name) {
        if (name == null || brand == null) {
            return null;
        }

        String normalizedName = name.trim();
        String normalizedBrand = brand.trim();

        if (normalizedName.regionMatches(true, 0, normalizedBrand, 0, normalizedBrand.length())) {
            String remainder = normalizedName.substring(normalizedBrand.length()).trim();
            if (!remainder.isBlank()) {
                return capitalizeWords(remainder.replaceAll("(?i)\\s+for\\s+(men|women|unisex)$", "").trim());
            }
        }

        String[] words = normalizedName.split("\\s+");
        if (words.length >= 2) {
            return capitalizeWords(words[0] + " " + words[1]);
        }

        return "Signature";
    }

    private static String buildDescription(FragellaFragranceResponse api) {
        List<String> accords = api.getMainAccords();
        if (accords != null && !accords.isEmpty()) {
            String accordText = accords.stream()
                    .limit(4)
                    .map(String::trim)
                    .collect(Collectors.joining(", "));
            return "A " + accordText + " composition by " + api.getBrand()
                    + ", crafted for " + defaultGenderLabel(api.getGender()) + ".";
        }

        return "A distinctive fragrance by " + api.getBrand() + ".";
    }

    private static String defaultGenderLabel(String gender) {
        if (gender == null || gender.isBlank()) {
            return "everyone";
        }

        return switch (gender.trim().toLowerCase(Locale.ROOT)) {
            case "men", "man" -> "him";
            case "women", "woman" -> "her";
            default -> "everyone";
        };
    }

    public static Double seedSampleRating(String brand, String name, int volumeMl) {
        return roundToOneDecimal(parseRatingFromApi(null, brand, name, volumeMl));
    }

    public static Integer seedSampleReviewCount(String brand, String name, int volumeMl) {
        int hash = Math.abs((brand + "|" + name + "|" + volumeMl).hashCode());
        return hash % 6;
    }

    private static double parseRatingFromApi(String apiRating, String brand, String name, int volumeMl) {
        if (apiRating != null && !apiRating.isBlank()) {
            try {
                return Double.parseDouble(apiRating.trim());
            } catch (NumberFormatException ignored) {
                // fall through to deterministic seed
            }
        }
        int hash = Math.abs((brand + "|" + name + "|" + volumeMl).hashCode());
        return 3.8 + (hash % 11) * 0.1;
    }

    private static double roundToOneDecimal(double value) {
        return Math.round(value * 10.0) / 10.0;
    }

    private static String capitalizeWords(String value) {
        if (value == null || value.isBlank()) {
            return value;
        }

        String[] parts = value.split("\\s+");
        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            if (part.isBlank()) {
                continue;
            }
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(Character.toUpperCase(part.charAt(0)));
            if (part.length() > 1) {
                builder.append(part.substring(1));
            }
        }
        return builder.toString();
    }

    private static double roundToTwoDecimals(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private static int parseYear(String year) {
        try {
            return year != null && !year.isBlank() ? Integer.parseInt(year.trim()) : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
