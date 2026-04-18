package com.uade.tpo.marketplacePerfume.controllers;

import java.net.URI;
import java.util.List;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.Sample.SampleRequestDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.Sample.SampleResponseDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.Sample.StockUpdateDTO;
import com.uade.tpo.marketplacePerfume.service.ISampleService;

@RestController
@RequestMapping("sample")
public class SampleController {

    @Autowired
    private ISampleService sampleService;

    @GetMapping("/all")
    public ResponseEntity<List<SampleResponseDTO>> getAll() {
        return ResponseEntity.ok(sampleService.getAllSamples());
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<SampleResponseDTO>> getSamplesBySellerId(@PathVariable Long sellerId) {
        List<SampleResponseDTO> samples = sampleService.getSamplesBySellerId(sellerId);
        if (samples.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(samples);
    }

    @GetMapping("/perfume/{perfumeId}")
    public ResponseEntity<List<SampleResponseDTO>> getSamplesByPerfumeId(@PathVariable Long perfumeId) {
        List<SampleResponseDTO> samples = sampleService.getSamplesByPerfumeId(perfumeId);
        if (samples.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(samples);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SampleResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(sampleService.getSampleByIdDTO(id));
    }

    @PostMapping
    public ResponseEntity<SampleResponseDTO> create(
            @RequestBody SampleRequestDTO sampleDto,
            @AuthenticationPrincipal User seller) {
        SampleResponseDTO created = sampleService.createSample(sampleDto, seller);
        return ResponseEntity.created(URI.create("/sample/" + created.getId())).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SampleResponseDTO> update(
            @PathVariable Long id,
            @RequestBody SampleRequestDTO sampleDto,
            @AuthenticationPrincipal User seller) {
        return ResponseEntity.ok(sampleService.updateSample(id, sampleDto, seller));
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<SampleResponseDTO> updateStock(
            @PathVariable Long id,
            @Valid @RequestBody StockUpdateDTO stockDto,
            @AuthenticationPrincipal User seller) {
        return ResponseEntity.ok(sampleService.updateSampleStock(id, stockDto.getStock(), seller));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User principal) {
        sampleService.deleteSample(id, principal);
        return ResponseEntity.ok("Sample successfully deleted");
    }
}
