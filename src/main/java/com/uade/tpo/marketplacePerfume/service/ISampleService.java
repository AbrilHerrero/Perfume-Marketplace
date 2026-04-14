package com.uade.tpo.marketplacePerfume.service;

import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.Sample;
import com.uade.tpo.marketplacePerfume.entity.dto.sampleDTOs.SampleRequestDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.sampleDTOs.SampleResponseDTO;

public interface ISampleService {
    List<SampleResponseDTO> getAllSamples();
    SampleResponseDTO getSampleByIdDTO(Long id);
    Sample getSampleById(Long id); // Para uso interno
    SampleResponseDTO createSample(SampleRequestDTO dto);
    SampleResponseDTO updateSample(Long id, SampleRequestDTO dto);
    void deleteSample(Long id);
}