package com.uade.tpo.marketplacePerfume.service.stats;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplacePerfume.entity.OrderStatus;
import com.uade.tpo.marketplacePerfume.entity.Role;
import com.uade.tpo.marketplacePerfume.entity.dto.stats.AdminStatsResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.stats.PublicStatsResponseDTO;
import com.uade.tpo.marketplacePerfume.repository.OrderItemRepository;
import com.uade.tpo.marketplacePerfume.repository.OrderRepository;
import com.uade.tpo.marketplacePerfume.repository.PerfumeRepository;
import com.uade.tpo.marketplacePerfume.repository.UserRepository;

@Service
public class StatsServiceImpl implements IStatsService {

    /** Orders still in flight — counted as "active" for the admin dashboard. */
    private static final List<OrderStatus> ACTIVE_STATUSES =
            List.of(OrderStatus.PENDING, OrderStatus.PAID, OrderStatus.SHIPPED);

    /** Orders whose decants have left the seller — counted as "shipped". */
    private static final List<OrderStatus> SHIPPED_STATUSES =
            List.of(OrderStatus.SHIPPED, OrderStatus.DELIVERED);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private PerfumeRepository perfumeRepository;

    @Override
    @Transactional(readOnly = true)
    public AdminStatsResponseDTO getAdminStats() {
        LocalDateTime startOfToday = LocalDate.now().atStartOfDay();
        LocalDateTime since30Days = LocalDateTime.now().minusDays(30);

        BigDecimal gmv = orderRepository.sumTotalSinceExcludingStatus(since30Days, OrderStatus.CANCELLED);

        return AdminStatsResponseDTO.builder()
                .users(userRepository.count())
                .sellers(userRepository.countByRoleAndActiveTrue(Role.SELLER))
                .activeOrdersToday(
                        orderRepository.countByCreatedAtGreaterThanEqualAndStatusIn(startOfToday, ACTIVE_STATUSES))
                .gmvLast30Days(gmv != null ? gmv : BigDecimal.ZERO)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public PublicStatsResponseDTO getPublicStats() {
        return PublicStatsResponseDTO.builder()
                .perfumes(perfumeRepository.count())
                .verifiedSellers(userRepository.countByRoleAndActiveTrue(Role.SELLER))
                .decantsShipped(orderItemRepository.sumQuantityByOrderStatuses(SHIPPED_STATUSES))
                .build();
    }
}
