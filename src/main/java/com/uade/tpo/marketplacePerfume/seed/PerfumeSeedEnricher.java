package com.uade.tpo.marketplacePerfume.seed;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.uade.tpo.marketplacePerfume.entity.Perfume;
import com.uade.tpo.marketplacePerfume.entity.PerfumeNotes;
import com.uade.tpo.marketplacePerfume.mapper.PerfumeMapper;

@Component
public class PerfumeSeedEnricher {

    private static final String[][] TOP_NOTES = {
            {"Bergamot", "Pink Pepper", "Mandarin"},
            {"Lemon", "Grapefruit", "Neroli"},
            {"Blackcurrant", "Pear", "Apple"},
            {"Cardamom", "Ginger", "Saffron"},
            {"Lavender", "Mint", "Green Notes"}
    };

    private static final String[][] MIDDLE_NOTES = {
            {"Iris Pallida", "Violet Leaf", "Suede"},
            {"Jasmine", "Rose", "Ylang-Ylang"},
            {"Cinnamon", "Nutmeg", "Clove"},
            {"Lily of the Valley", "Peony", "Freesia"},
            {"Geranium", "Patchouli", "Cedar"}
    };

    private static final String[][] BASE_NOTES = {
            {"Vetiver", "Smoked Cedar", "Ambrette"},
            {"Vanilla", "Tonka Bean", "Amber"},
            {"Musk", "Sandalwood", "Oakmoss"},
            {"Leather", "Tobacco", "Oud"},
            {"Benzoin", "Labdanum", "Incense"}
    };

    private static final String[][] ACCORD_SETS = {
            {"Powdery", "Woody", "Smoky", "Leather"},
            {"Fresh", "Citrus", "Aromatic", "Green"},
            {"Floral", "Sweet", "Fruity", "Musky"},
            {"Oriental", "Spicy", "Warm", "Amber"},
            {"Aquatic", "Marine", "Clean", "Mineral"}
    };

    public void enrichIfNeeded(Perfume perfume) {
        if (perfume == null || perfume.getDescription() != null) {
            return;
        }

        Random random = new Random(seed(perfume));
        int profile = random.nextInt(TOP_NOTES.length);

        perfume.setLine(PerfumeMapper.extractLine(perfume.getBrand(), perfume.getName()));
        perfume.setNotes(PerfumeNotes.builder()
                .top(pickNotes(TOP_NOTES[profile], random, 2))
                .middle(pickNotes(MIDDLE_NOTES[profile], random, 3))
                .base(pickNotes(BASE_NOTES[profile], random, 3))
                .build());
        perfume.setAccords(List.of(ACCORD_SETS[profile]));
        perfume.setDescription(buildDescription(perfume));
    }

    private String buildDescription(Perfume perfume) {
        String accordText = String.join(", ", perfume.getAccords()).toLowerCase(Locale.ROOT);
        return "A " + accordText + " fragrance from " + perfume.getBrand()
                + ". Opens with brightness, settles into a refined dry-down with lasting character.";
    }

    private List<String> pickNotes(String[] pool, Random random, int count) {
        List<String> selected = new ArrayList<>();
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < pool.length; i++) {
            indexes.add(i);
        }

        int picks = Math.min(count, pool.length);
        for (int i = 0; i < picks; i++) {
            int index = indexes.remove(random.nextInt(indexes.size()));
            selected.add(pool[index]);
        }
        return selected;
    }

    private long seed(Perfume perfume) {
        String key = perfume.getId() + "|" + perfume.getBrand() + "|" + perfume.getName();
        return key.hashCode();
    }
}
