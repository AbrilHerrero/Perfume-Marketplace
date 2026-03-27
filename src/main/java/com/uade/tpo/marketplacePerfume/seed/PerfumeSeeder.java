package com.uade.tpo.marketplacePerfume.seed;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.uade.tpo.marketplacePerfume.adapter.FragellaApiAdapter;
import com.uade.tpo.marketplacePerfume.adapter.dto.FragellaFragranceResponse;
import com.uade.tpo.marketplacePerfume.entity.Perfume;
import com.uade.tpo.marketplacePerfume.mapper.PerfumeMapper;
import com.uade.tpo.marketplacePerfume.repository.PerfumeRepository;

@Component
public class PerfumeSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(PerfumeSeeder.class);

    private final PerfumeRepository perfumeRepository;
    private final FragellaApiAdapter fragellaApiClient;

    private static final List<String> BRANDS = List.of(
            "Dior", "Chanel", "Yves Saint Laurent", "Tom Ford",
            "Versace", "Giorgio Armani", "Creed", "Jean Paul Gaultier",
            "Prada", "Dolce Gabbana"
    );
    private static final int LIMIT_PER_BRAND = 5;

    public PerfumeSeeder(PerfumeRepository perfumeRepository, FragellaApiAdapter fragellaApiClient) {
        this.perfumeRepository = perfumeRepository;
        this.fragellaApiClient = fragellaApiClient;
    }

    @Override
    public void run(String... args) {
        if (perfumeRepository.count() > 0) {
            log.info("Database already seeded. Skipping.");
            return;
        }

        log.info("Seeding perfume database...");
        int totalSaved = 0;

        for (String brand : BRANDS) {
            try {
                List<FragellaFragranceResponse> fragrances =
                        fragellaApiClient.searchFragrances(brand, LIMIT_PER_BRAND);

                for (FragellaFragranceResponse apiResponse : fragrances) {
                    Perfume perfume = PerfumeMapper.fromApiResponse(apiResponse);
                    perfumeRepository.save(perfume);
                    totalSaved++;
                }
                log.info("Seeded {} perfumes for brand: {}", fragrances.size(), brand);
            } catch (Exception e) {
                log.warn("Failed to seed brand {}: {}", brand, e.getMessage());
            }
        }
        log.info("Seeding complete. Total perfumes saved: {}", totalSaved);
    }
}
