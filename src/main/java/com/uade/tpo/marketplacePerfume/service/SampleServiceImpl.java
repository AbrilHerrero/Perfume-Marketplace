package com.uade.tpo.marketplacePerfume.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplacePerfume.entity.Sample;
import com.uade.tpo.marketplacePerfume.entity.dto.sampleDTOs.SampleRequestDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.sampleDTOs.SampleResponseDTO;
import com.uade.tpo.marketplacePerfume.exceptions.SampleNotFoundException;
import com.uade.tpo.marketplacePerfume.repository.SampleRepository;

@Service
public class SampleServiceImpl implements ISampleService {

    @Autowired
    private SampleRepository sampleRepository;

    @Override
    public List<SampleResponseDTO> getAllSamples() {
        return sampleRepository.findAll().stream()
                .filter(Sample::isActive)
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public SampleResponseDTO getSampleByIdDTO(Long id) {
        return mapToResponseDTO(getSampleById(id));
    }

    @Override
    public Sample getSampleById(Long id) {
        return sampleRepository.findById(id)
                .orElseThrow(() -> new SampleNotFoundException(id));
    }

    @Override
    public SampleResponseDTO createSample(SampleRequestDTO dto) {
        Sample sample = new Sample();
        sample.setPrice(dto.getPrice());
        sample.setStock(dto.getStock());
        sample.setVolumeMl(dto.getVolumeMl());
        sample.setDescription(dto.getDescription());
        sample.setImageUrl(dto.getImageUrl());
        Sample savedSample = sampleRepository.save(sample);
        return mapToResponseDTO(savedSample);
    }

    @Override
    public SampleResponseDTO updateSample(Long id, SampleRequestDTO dto) {
        Sample existing = getSampleById(id);
        if (dto.getPrice() != null) existing.setPrice(dto.getPrice());
        if (dto.getStock() != null) existing.setStock(dto.getStock());
        if (dto.getVolumeMl() != null) existing.setVolumeMl(dto.getVolumeMl());
        if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if (dto.getImageUrl() != null) existing.setImageUrl(dto.getImageUrl());
        return mapToResponseDTO(sampleRepository.save(existing));
    }

    @Override
    public void deleteSample(Long id) {
        Sample sample = getSampleById(id);
        sample.setActive(false);
        sampleRepository.save(sample);
    }

    private SampleResponseDTO mapToResponseDTO(Sample sample) {
        SampleResponseDTO response = new SampleResponseDTO();
        response.setId(sample.getId());
        response.setPrice(sample.getPrice());
        response.setStock(sample.getStock());
        response.setVolumeMl(sample.getVolumeMl());
        response.setDescription(sample.getDescription());
        response.setImageUrl(sample.getImageUrl());
        return response;
    }
}