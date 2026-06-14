package com.uade.tpo.marketplacePerfume.seed;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.uade.tpo.marketplacePerfume.entity.Perfume;
import com.uade.tpo.marketplacePerfume.entity.Role;
import com.uade.tpo.marketplacePerfume.entity.Sample;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.mapper.PerfumeMapper;
import com.uade.tpo.marketplacePerfume.repository.PerfumeRepository;
import com.uade.tpo.marketplacePerfume.repository.SampleRepository;
import com.uade.tpo.marketplacePerfume.repository.UserRepository;

@Component
@Order(2)
public class SampleSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SampleSeeder.class);
    private static final String DEFAULT_SELLER_PASSWORD = "seller123";
    private static final int[] SAMPLE_VOLUMES_ML = {5, 10};
    private static final BigDecimal[] SAMPLE_PRICES = {
            BigDecimal.valueOf(18.50),
            BigDecimal.valueOf(32.00)
    };
    private static final int DEFAULT_STOCK = 15;

    private static final List<SellerSeedData> SELLER_SEEDS = List.of(
            new SellerSeedData("María", "García", "seller.maria@marketplace.com"),
            new SellerSeedData("Carlos", "López", "seller.carlos@marketplace.com"),
            new SellerSeedData("Ana", "Martínez", "seller.ana@marketplace.com")
    );

    private final PerfumeRepository perfumeRepository;
    private final SampleRepository sampleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SampleSeeder(PerfumeRepository perfumeRepository,
                        SampleRepository sampleRepository,
                        UserRepository userRepository,
                        PasswordEncoder passwordEncoder) {
        this.perfumeRepository = perfumeRepository;
        this.sampleRepository = sampleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (sampleRepository.count() > 0) {
            log.info("Samples already seeded. Skipping.");
            return;
        }

        List<Perfume> perfumes = perfumeRepository.findAll();
        if (perfumes.isEmpty()) {
            log.info("No perfumes found. Skipping sample seeding.");
            return;
        }

        log.info("Seeding samples and sellers...");
        List<User> sellers = ensureSellersExist();
        int samplesCreated = seedSamplesForPerfumes(perfumes, sellers);

        log.info("Sample seeding complete. Sellers: {}, samples created: {}",
                sellers.size(), samplesCreated);
    }

    private List<User> ensureSellersExist() {
        List<User> sellers = new ArrayList<>();

        for (SellerSeedData seed : SELLER_SEEDS) {
            User seller = userRepository.findByEmail(seed.email())
                    .orElseGet(() -> userRepository.save(User.builder()
                            .name(seed.name())
                            .surname(seed.surname())
                            .email(seed.email())
                            .password(passwordEncoder.encode(DEFAULT_SELLER_PASSWORD))
                            .telephone("1100000000")
                            .registerDate(LocalDate.now())
                            .active(true)
                            .role(Role.SELLER)
                            .build()));
            sellers.add(seller);
            log.info("Seller ready: {} ({})", seller.getEmail(), seller.getId());
        }

        return sellers;
    }

    private int seedSamplesForPerfumes(List<Perfume> perfumes, List<User> sellers) {
        int samplesCreated = 0;

        for (int i = 0; i < perfumes.size(); i++) {
            Perfume perfume = perfumes.get(i);
            User seller = sellers.get(i % sellers.size());

            for (int volumeIndex = 0; volumeIndex < SAMPLE_VOLUMES_ML.length; volumeIndex++) {
                int volumeMl = SAMPLE_VOLUMES_ML[volumeIndex];
                Sample sample = Sample.builder()
                        .perfume(perfume)
                        .seller(seller)
                        .volumeMl(volumeMl)
                        .price(SAMPLE_PRICES[volumeIndex])
                        .stock(DEFAULT_STOCK)
                        .description(buildDescription(perfume, volumeMl))
                        .imageUrl(resolveImageUrl(perfume))
                        .rating(PerfumeMapper.seedSampleRating(perfume.getBrand(), perfume.getName(), volumeMl))
                        .reviewCount(PerfumeMapper.seedSampleReviewCount(perfume.getBrand(), perfume.getName(), volumeMl))
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .build();

                sampleRepository.save(sample);
                samplesCreated++;
            }
        }

        return samplesCreated;
    }

    private String buildDescription(Perfume perfume, int volumeMl) {
        return "Muestra de " + volumeMl + "ml de " + perfume.getName()
                + " (" + perfume.getBrand() + ")";
    }

    private String resolveImageUrl(Perfume perfume) {
        if (perfume.getImageUrl() != null && !perfume.getImageUrl().isBlank()) {
            return perfume.getImageUrl();
        }
        return "https://placehold.co/400x400?text=Sample";
    }

    private record SellerSeedData(String name, String surname, String email) {
    }
}
