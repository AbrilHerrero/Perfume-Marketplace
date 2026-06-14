package com.uade.tpo.marketplacePerfume.seed;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.uade.tpo.marketplacePerfume.entity.Review;
import com.uade.tpo.marketplacePerfume.entity.Role;
import com.uade.tpo.marketplacePerfume.entity.Sample;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.repository.ReviewRepository;
import com.uade.tpo.marketplacePerfume.repository.SampleRepository;
import com.uade.tpo.marketplacePerfume.repository.UserRepository;

/**
 * Seeds reviews from the buyer accounts and derives each sample's rating and
 * review count from those real reviews, so the rating shown on a card always
 * matches what the review list returns.
 */
@Component
@Order(4)
public class ReviewSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(ReviewSeeder.class);

    private static final String[] COMMENTS = {
            "Long-lasting and true to the original. Worth it.",
            "Lovely projection the first couple of hours.",
            "Nice decant, arrived well packed.",
            "Good value for the price.",
            "Pleasant scent, a bit faint on me.",
            null, // rating-only review
    };

    private final ReviewRepository reviewRepository;
    private final SampleRepository sampleRepository;
    private final UserRepository userRepository;

    public ReviewSeeder(ReviewRepository reviewRepository,
                        SampleRepository sampleRepository,
                        UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.sampleRepository = sampleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) {
        if (reviewRepository.count() > 0) {
            log.info("Reviews already seeded. Skipping.");
            return;
        }

        List<User> buyers = userRepository.findByRoleAndActiveTrue(Role.BUYER);
        List<Sample> samples = sampleRepository.findAll();
        if (buyers.isEmpty() || samples.isEmpty()) {
            log.info("No buyers or samples found. Skipping review seeding.");
            return;
        }

        int reviewsCreated = 0;
        for (Sample sample : samples) {
            reviewsCreated += seedReviewsForSample(sample, buyers);
        }

        log.info("Review seeding complete. Reviews created: {}", reviewsCreated);
    }

    private int seedReviewsForSample(Sample sample, List<User> buyers) {
        // Deterministic 1..min(3, buyers) reviews per sample so every sample has
        // at least one review and the catalog isn't full of empty ratings.
        int seed = Math.abs(Long.hashCode(sample.getId()));
        int reviewCount = 1 + (seed % Math.min(3, buyers.size()));

        List<Integer> ratings = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < reviewCount; i++) {
            User buyer = buyers.get(i % buyers.size());
            int rating = 3 + ((seed / (i + 1)) % 3); // 3..5
            String comment = COMMENTS[(seed + i) % COMMENTS.length];

            reviewRepository.save(Review.builder()
                    .buyer(buyer)
                    .sample(sample)
                    .rating(rating)
                    .comment(comment)
                    .createdAt(now.minusDays((seed + i) % 30))
                    .updatedAt(now.minusDays((seed + i) % 30))
                    .build());
            ratings.add(rating);
        }

        double average = ratings.stream().mapToInt(Integer::intValue).average().orElse(0);
        sample.setRating(Math.round(average * 10.0) / 10.0);
        sample.setReviewCount(ratings.size());
        sampleRepository.save(sample);

        return ratings.size();
    }
}
