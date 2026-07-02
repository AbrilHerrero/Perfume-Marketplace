package com.uade.tpo.marketplacePerfume.service.coupon;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.uade.tpo.marketplacePerfume.entity.Coupon;
import com.uade.tpo.marketplacePerfume.entity.CouponRedemption;
import com.uade.tpo.marketplacePerfume.entity.DiscountType;
import com.uade.tpo.marketplacePerfume.entity.Order;
import com.uade.tpo.marketplacePerfume.entity.Role;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.coupon.CouponCreateDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.coupon.CouponRedemptionResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.coupon.CouponResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.coupon.CouponUpdateDTO;
import com.uade.tpo.marketplacePerfume.exceptions.coupon.CouponAlreadyUsedException;
import com.uade.tpo.marketplacePerfume.exceptions.coupon.CouponCodeAlreadyExistsException;
import com.uade.tpo.marketplacePerfume.exceptions.coupon.CouponExpiredException;
import com.uade.tpo.marketplacePerfume.exceptions.coupon.CouponInvalidException;
import com.uade.tpo.marketplacePerfume.exceptions.coupon.CouponNotFoundException;
import com.uade.tpo.marketplacePerfume.exceptions.coupon.CouponNotOwnedException;
import com.uade.tpo.marketplacePerfume.exceptions.user.UserNotFoundException;
import com.uade.tpo.marketplacePerfume.mapper.CouponMapper;
import com.uade.tpo.marketplacePerfume.mapper.CouponRedemptionMapper;
import com.uade.tpo.marketplacePerfume.repository.CouponRedemptionRepository;
import com.uade.tpo.marketplacePerfume.repository.CouponRepository;
import com.uade.tpo.marketplacePerfume.repository.UserRepository;

@Service
public class CouponServiceImpl implements ICouponService {

    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponRedemptionRepository couponRedemptionRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public CouponResponseDTO createCoupon(CouponCreateDTO dto, User sellerPrincipal) {
        validateConfiguration(dto.getDiscountType(), dto.getDiscountValue(), dto.getValidFrom(), dto.getValidUntil());

        if (couponRepository.existsByCode(dto.getCode())) {
            throw new CouponCodeAlreadyExistsException();
        }

        Coupon coupon = CouponMapper.toEntityFromCreate(dto);
        coupon.setCreatedAt(LocalDateTime.now());

        User seller = userRepository.findById(sellerPrincipal.getId()).orElseThrow(UserNotFoundException::new);
        coupon.setSeller(seller);

        return CouponMapper.toResponseDto(couponRepository.save(coupon));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CouponResponseDTO> getMyCoupons(User seller) {
        return CouponMapper.toResponseDtoList(couponRepository.findBySeller_Id(seller.getId()));
    }

    @Override
    @Transactional
    public CouponResponseDTO updateCoupon(Long id, CouponUpdateDTO dto, User principal) {
        Coupon existing = couponRepository.findById(id).orElseThrow(CouponNotFoundException::new);
        assertCanManage(existing, principal);

        CouponMapper.applyUpdate(dto, existing);
        validateConfiguration(existing.getDiscountType(), existing.getDiscountValue(),
                existing.getValidFrom(), existing.getValidUntil());

        return CouponMapper.toResponseDto(couponRepository.save(existing));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CouponRedemptionResponseDTO> getMyCouponHistory(User seller) {
        return CouponRedemptionMapper.toResponseDtoList(
                couponRedemptionRepository.findByCoupon_Seller_Id(seller.getId()));
    }

    @Override
    @Transactional(readOnly = true)
    public Coupon validateForRedemption(String code, User buyer) {
        Coupon coupon = couponRepository.findByCode(code).orElseThrow(CouponNotFoundException::new);

        if (!coupon.isActive()) {
            throw new CouponExpiredException();
        }

        LocalDateTime now = LocalDateTime.now();
        if (coupon.getValidFrom() != null && now.isBefore(coupon.getValidFrom())) {
            throw new CouponExpiredException();
        }
        if (coupon.getValidUntil() != null && now.isAfter(coupon.getValidUntil())) {
            throw new CouponExpiredException();
        }

        if (couponRedemptionRepository.existsByCoupon_IdAndBuyer_Id(coupon.getId(), buyer.getId())) {
            throw new CouponAlreadyUsedException();
        }

        return coupon;
    }

    @Override
    public BigDecimal computeDiscount(Coupon coupon, BigDecimal sellerSubtotal) {
        if (coupon == null || sellerSubtotal == null || sellerSubtotal.signum() <= 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal discount;
        if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {
            discount = sellerSubtotal.multiply(coupon.getDiscountValue())
                    .divide(HUNDRED, 2, RoundingMode.HALF_UP);
        } else {
            discount = coupon.getDiscountValue();
        }

        // Never discount more than the seller's subtotal.
        if (discount.compareTo(sellerSubtotal) > 0) {
            discount = sellerSubtotal;
        }
        return discount;
    }

    @Override
    @Transactional
    public CouponRedemption recordRedemption(Coupon coupon, User buyer, Order order, BigDecimal discount) {
        CouponRedemption redemption = CouponRedemption.builder()
                .coupon(coupon)
                .buyer(buyer)
                .order(order)
                .discountAmount(discount)
                .redeemedAt(LocalDateTime.now())
                .build();
        return couponRedemptionRepository.save(redemption);
    }

    private void assertCanManage(Coupon coupon, User principal) {
        if (principal != null && principal.getRole() == Role.ADMIN) {
            return;
        }
        if (principal == null || coupon.getSeller() == null
                || !coupon.getSeller().getId().equals(principal.getId())) {
            throw new CouponNotOwnedException();
        }
    }

    private void validateConfiguration(DiscountType type, BigDecimal value,
            LocalDateTime validFrom, LocalDateTime validUntil) {
        if (type == null || value == null || value.signum() <= 0) {
            throw new CouponInvalidException();
        }
        if (type == DiscountType.PERCENTAGE && value.compareTo(HUNDRED) > 0) {
            throw new CouponInvalidException();
        }
        if (validFrom != null && validUntil != null && validUntil.isBefore(validFrom)) {
            throw new CouponInvalidException();
        }
    }
}
