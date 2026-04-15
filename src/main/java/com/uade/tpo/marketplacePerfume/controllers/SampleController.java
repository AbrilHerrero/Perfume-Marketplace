package com.uade.tpo.marketplacePerfume.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplacePerfume.entity.dto.sampleDTOs.SampleRequestDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.sampleDTOs.SampleResponseDTO;
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

    @GetMapping("/{id}")
    public ResponseEntity<SampleResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(sampleService.getSampleByIdDTO(id));
    }

    @PostMapping
    public ResponseEntity<SampleResponseDTO> create(@RequestBody SampleRequestDTO sampleDto) {
        return ResponseEntity.ok(sampleService.createSample(sampleDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SampleResponseDTO> update(@PathVariable Long id, @RequestBody SampleRequestDTO sampleDto) {
        return ResponseEntity.ok(sampleService.updateSample(id, sampleDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sampleService.deleteSample(id);
        return ResponseEntity.noContent().build();
    }
}
