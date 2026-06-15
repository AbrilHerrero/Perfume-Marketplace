package com.uade.tpo.marketplacePerfume.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplacePerfume.entity.dto.stats.PublicStatsResponseDTO;
import com.uade.tpo.marketplacePerfume.service.stats.IStatsService;

@RestController
@RequestMapping("stats")
public class StatsController {

    @Autowired
    private IStatsService statsService;

    @GetMapping
    public ResponseEntity<PublicStatsResponseDTO> getPublicStats() {
        return ResponseEntity.ok(statsService.getPublicStats());
    }
}
