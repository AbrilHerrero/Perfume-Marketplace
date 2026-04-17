package com.uade.tpo.marketplacePerfume.service;

import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.Sample;
import com.uade.tpo.marketplacePerfume.entity.dto.Sample.SampleRequestDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.Sample.SampleResponseDTO;

public interface ISampleService {
    List<SampleResponseDTO> getAllSamples();
    SampleResponseDTO getSampleByIdDTO(Long id);
    Sample getSampleById(Long id);
    SampleResponseDTO createSample(SampleRequestDTO dto);
    SampleResponseDTO updateSample(Long id, SampleRequestDTO dto);
    void deleteSample(Long id);
    List<SampleResponseDTO> getSamplesBySellerId(Long sellerId); // Nuevo
}