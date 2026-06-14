package com.uade.tpo.marketplacePerfume.seed;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.uade.tpo.marketplacePerfume.entity.Coupon;
import com.uade.tpo.marketplacePerfume.entity.CouponRedemption;
import com.uade.tpo.marketplacePerfume.entity.DiscountType;
import com.uade.tpo.marketplacePerfume.entity.Order;
import com.uade.tpo.marketplacePerfume.entity.OrderItem;
import com.uade.tpo.marketplacePerfume.entity.OrderStatus;
import com.uade.tpo.marketplacePerfume.entity.Role;
import com.uade.tpo.marketplacePerfume.entity.Sample;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.repository.CouponRedemptionRepository;
import com.uade.tpo.marketplacePerfume.repository.CouponRepository;
import com.uade.tpo.marketplacePerfume.repository.OrderItemRepository;
import com.uade.tpo.marketplacePerfume.repository.OrderRepository;
import com.uade.tpo.marketplacePerfume.repository.SampleRepository;
import com.uade.tpo.marketplacePerfume.repository.UserRepository;

/**
 * Seeds, for each seller, two discount coupons plus a handful of recent buyer
 * orders — some redeeming the seller's coupons — so the seller dashboard shows
 * real coupon usage, redemption history and 30-day sales figures.
 */
@Component
@org.springframework.core.annotation.Order(3)
public class SalesSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(SalesSeeder.class);
    private static final String BUYER_PASSWORD = "buyer123";

    private static final List<BuyerSeed> BUYER_SEEDS = List.of(
            new BuyerSeed("Sofía", "Romero", "buyer.sofia@marketplace.com"),
            new BuyerSeed("Diego", "Fernández", "buyer.diego@marketplace.com"),
            new BuyerSeed("Lucía", "Pérez", "buyer.lucia@marketplace.com"));

    private final UserRepository userRepository;
    private final SampleRepository sampleRepository;
    private final CouponRepository couponRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CouponRedemptionRepository couponRedemptionRepository;
    private final PasswordEncoder passwordEncoder;

    public SalesSeeder(UserRepository userRepository,
                       SampleRepository sampleRepository,
                       CouponRepository couponRepository,
                       OrderRepository orderRepository,
                       OrderItemRepository orderItemRepository,
                       CouponRedemptionRepository couponRedemptionRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.sampleRepository = sampleRepository;
        this.couponRepository = couponRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.couponRedemptionRepository = couponRedemptionRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (orderRepository.count() > 0) {
            log.info("Sales already seeded. Skipping.");
            return;
        }

        List<User> sellers = userRepository.findByRoleAndActiveTrue(Role.SELLER);
        if (sellers.isEmpty()) {
            log.info("No sellers found. Skipping sales seeding.");
            return;
        }

        log.info("Seeding coupons, buyers and orders...");
        List<User> buyers = ensureBuyers();

        int couponsCreated = 0;
        int ordersCreated = 0;
        for (User seller : sellers) {
            List<Sample> samples = sampleRepository.findBySeller_Id(seller.getId());
            if (samples.isEmpty()) {
                log.info("Seller {} has no samples. Skipping.", seller.getEmail());
                continue;
            }
            List<Coupon> coupons = ensureCouponsForSeller(seller);
            couponsCreated += coupons.size();
            ordersCreated += seedOrdersForSeller(seller, samples, coupons, buyers);
        }

        log.info("Sales seeding complete. Buyers: {}, coupons: {}, orders: {}",
                buyers.size(), couponsCreated, ordersCreated);
    }

    private List<User> ensureBuyers() {
        List<User> buyers = new ArrayList<>();
        for (BuyerSeed seed : BUYER_SEEDS) {
            User buyer = userRepository.findByEmail(seed.email())
                    .orElseGet(() -> userRepository.save(User.builder()
                            .name(seed.name())
                            .surname(seed.surname())
                            .email(seed.email())
                            .password(passwordEncoder.encode(BUYER_PASSWORD))
                            .telephone("1100000000")
                            .registerDate(LocalDate.now())
                            .active(true)
                            .role(Role.BUYER)
                            .build()));
            buyers.add(buyer);
        }
        return buyers;
    }

    private List<Coupon> ensureCouponsForSeller(User seller) {
        String base = couponBaseFromName(seller.getName());

        Coupon percentage = ensureCoupon(base + "10", DiscountType.PERCENTAGE,
                BigDecimal.valueOf(10), BigDecimal.valueOf(20), 50, seller);
        Coupon fixed = ensureCoupon(base + "5", DiscountType.FIXED,
                BigDecimal.valueOf(5), BigDecimal.ZERO, 100, seller);
        return List.of(percentage, fixed);
    }

    // "María" -> "MARIA": strip accents (NFD then drop combining marks) so
    // coupon codes don't lose accented letters.
    private String couponBaseFromName(String name) {
        if (name == null) {
            return "SELLER";
        }
        String stripped = java.text.Normalizer.normalize(name, java.text.Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toUpperCase()
                .replaceAll("[^A-Z0-9]", "");
        return stripped.isBlank() ? "SELLER" : stripped;
    }

    private Coupon ensureCoupon(String code, DiscountType type, BigDecimal value,
            BigDecimal minOrder, int maxUses, User seller) {
        return couponRepository.findByCode(code)
                .orElseGet(() -> couponRepository.save(Coupon.builder()
                        .code(code)
                        .discountType(type)
                        .discountValue(value)
                        .minOrder(minOrder)
                        .maxUses(maxUses)
                        .validFrom(LocalDateTime.now().minusDays(30))
                        .validUntil(LocalDateTime.now().plusDays(60))
                        .active(true)
                        .createdAt(LocalDateTime.now())
                        .seller(seller)
                        .build()));
    }

    private int seedOrdersForSeller(User seller, List<Sample> samples,
            List<Coupon> coupons, List<User> buyers) {
        Sample s0 = samples.get(0 % samples.size());
        Sample s1 = samples.get(1 % samples.size());
        Sample s2 = samples.get(2 % samples.size());
        Sample s3 = samples.get(3 % samples.size());

        User b0 = buyers.get(0 % buyers.size());
        User b1 = buyers.get(1 % buyers.size());
        User b2 = buyers.get(2 % buyers.size());

        Coupon percentage = coupons.get(0);
        Coupon fixed = coupons.get(1);
        LocalDateTime now = LocalDateTime.now();

        createOrder(b0, List.of(new OrderLine(s0, 2), new OrderLine(s1, 1)), null,
                OrderStatus.DELIVERED, now.minusDays(12));
        createOrder(b1, List.of(new OrderLine(s2, 3)), percentage,
                OrderStatus.PAID, now.minusDays(6));
        createOrder(b2, List.of(new OrderLine(s3, 2)), fixed,
                OrderStatus.DELIVERED, now.minusDays(3));
        createOrder(b0, List.of(new OrderLine(s0, 1), new OrderLine(s2, 1)), percentage,
                OrderStatus.DELIVERED, now.minusDays(2));
        createOrder(b1, List.of(new OrderLine(s1, 1)), null,
                OrderStatus.SHIPPED, now.minusDays(1));
        return 5;
    }

    private void createOrder(User buyer, List<OrderLine> lines, Coupon coupon,
            OrderStatus status, LocalDateTime createdAt) {
        Order order = orderRepository.save(Order.builder()
                .buyer(buyer)
                .createdAt(createdAt)
                .status(status)
                .total(BigDecimal.ZERO)
                .orderItems(new ArrayList<>())
                .build());

        BigDecimal subtotal = BigDecimal.ZERO;
        List<OrderItem> items = new ArrayList<>();
        for (OrderLine line : lines) {
            Sample sample = line.sample();
            items.add(orderItemRepository.save(OrderItem.builder()
                    .order(order)
                    .sample(sample)
                    .quantity(line.quantity())
                    .unitPrice(sample.getPrice())
                    .build()));
            subtotal = subtotal.add(
                    sample.getPrice().multiply(BigDecimal.valueOf(line.quantity())));
            sample.setStock(Math.max(0, sample.getStock() - line.quantity()));
            sampleRepository.save(sample);
        }

        BigDecimal discount = BigDecimal.ZERO;
        if (coupon != null) {
            discount = computeDiscount(coupon, subtotal);
            order.setCouponCode(coupon.getCode());
            order.setDiscountAmount(discount);
        }
        order.setOrderItems(items);
        order.setTotal(subtotal.subtract(discount));
        orderRepository.save(order);

        if (coupon != null) {
            couponRedemptionRepository.save(CouponRedemption.builder()
                    .coupon(coupon)
                    .buyer(buyer)
                    .order(order)
                    .discountAmount(discount)
                    .redeemedAt(createdAt)
                    .build());
        }
    }

    private BigDecimal computeDiscount(Coupon coupon, BigDecimal subtotal) {
        BigDecimal discount;
        if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {
            discount = subtotal.multiply(coupon.getDiscountValue())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else {
            discount = coupon.getDiscountValue();
        }
        return discount.min(subtotal);
    }

    private record BuyerSeed(String name, String surname, String email) {
    }

    private record OrderLine(Sample sample, int quantity) {
    }
}
