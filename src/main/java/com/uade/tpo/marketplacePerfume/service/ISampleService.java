package com.uade.tpo.marketplacePerfume.service;

import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.Sample;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.Sample.SampleRequestDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.Sample.SampleResponseDTO;

public interface ISampleService {
    List<SampleResponseDTO> getAllSamples();
    SampleResponseDTO getSampleByIdDTO(Long id);
    Sample getSampleById(Long id);
    SampleResponseDTO createSample(SampleRequestDTO dto, User seller);
    SampleResponseDTO updateSample(Long id, SampleRequestDTO dto, User seller);
    SampleResponseDTO updateSampleStock(Long id, Integer newStock, User sellerPrincipal);
    void deleteSample(Long id, User principal);
    List<SampleResponseDTO> getSamplesBySellerId(Long sellerId);
    List<SampleResponseDTO> getSamplesByPerfumeId(Long perfumeId);
}