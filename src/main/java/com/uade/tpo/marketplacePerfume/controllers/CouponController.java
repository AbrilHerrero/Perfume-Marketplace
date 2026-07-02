package com.uade.tpo.marketplacePerfume.controllers;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.coupon.CouponCreateDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.coupon.CouponRedemptionResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.coupon.CouponResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.coupon.CouponUpdateDTO;
import com.uade.tpo.marketplacePerfume.service.coupon.ICouponService;

@RestController
@RequestMapping("coupons")
public class CouponController {

    @Autowired
    private ICouponService couponService;

    @PostMapping
    public ResponseEntity<CouponResponseDTO> create(
            @Valid @RequestBody CouponCreateDTO couponDto,
            @AuthenticationPrincipal User seller) {
        CouponResponseDTO created = couponService.createCoupon(couponDto, seller);
        return ResponseEntity.created(URI.create("/coupons/" + created.getId())).body(created);
    }

    @GetMapping("/mine")
    public ResponseEntity<List<CouponResponseDTO>> getMine(@AuthenticationPrincipal User seller) {
        return ResponseEntity.ok(couponService.getMyCoupons(seller));
    }

    @GetMapping("/history")
    public ResponseEntity<List<CouponRedemptionResponseDTO>> getMyHistory(@AuthenticationPrincipal User seller) {
        return ResponseEntity.ok(couponService.getMyCouponHistory(seller));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CouponResponseDTO> update(
            @PathVariable Long id,
            @RequestBody CouponUpdateDTO couponDto,
            @AuthenticationPrincipal User principal) {
        return ResponseEntity.ok(couponService.updateCoupon(id, couponDto, principal));
    }
}
