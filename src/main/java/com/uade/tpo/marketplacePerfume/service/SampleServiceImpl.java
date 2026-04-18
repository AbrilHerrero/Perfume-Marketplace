package com.uade.tpo.marketplacePerfume.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplacePerfume.entity.Perfume;
import com.uade.tpo.marketplacePerfume.entity.Role;
import com.uade.tpo.marketplacePerfume.entity.Sample;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.Sample.SampleRequestDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.Sample.SampleResponseDTO;
import com.uade.tpo.marketplacePerfume.exceptions.PerfumeNotFoundException;
import com.uade.tpo.marketplacePerfume.exceptions.SampleIncompleteRequestException;
import com.uade.tpo.marketplacePerfume.exceptions.SampleNotFoundException;
import com.uade.tpo.marketplacePerfume.exceptions.SampleNotOwnedForDeleteException;
import com.uade.tpo.marketplacePerfume.exceptions.SampleNotOwnedForUpdateException;
import com.uade.tpo.marketplacePerfume.exceptions.StockUpdateInvalidException;
import com.uade.tpo.marketplacePerfume.exceptions.UserNotFoundException;
import com.uade.tpo.marketplacePerfume.mapper.SampleMapper;
import com.uade.tpo.marketplacePerfume.repository.PerfumeRepository;
import com.uade.tpo.marketplacePerfume.repository.SampleRepository;
import com.uade.tpo.marketplacePerfume.repository.UserRepository;

@Service
public class SampleServiceImpl implements ISampleService {

    @Autowired
    private SampleRepository sampleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PerfumeRepository perfumeRepository;

    @Override
    public List<SampleResponseDTO> getAllSamples() {
        return SampleMapper.toResponseDtoList(sampleRepository.findByActiveTrue());
    }

    @Override
    public List<SampleResponseDTO> getSamplesBySellerId(Long sellerId) {
        return SampleMapper.toResponseDtoList(sampleRepository.findBySeller_IdAndActiveTrue(sellerId));
    }

    @Override
    public List<SampleResponseDTO> getSamplesByPerfumeId(Long perfumeId) {
        if (!perfumeRepository.existsById(perfumeId)) {
            throw new PerfumeNotFoundException();
        }
        return SampleMapper.toResponseDtoList(sampleRepository.findByPerfume_IdAndActiveTrue(perfumeId));
    }

    @Override
    public SampleResponseDTO getSampleByIdDTO(Long id) {
        return SampleMapper.toResponseDto(findActiveByIdOrThrow(id));
    }

    @Override
    public Sample getSampleById(Long id) {
        return findActiveByIdOrThrow(id);
    }

    @Override
    public SampleResponseDTO createSample(SampleRequestDTO dto, User sellerPrincipal) {
        validateSampleRequestComplete(dto);

        Sample sample = SampleMapper.toEntityFromRequest(dto);
        sample.setCreatedAt(LocalDateTime.now());

        User seller = userRepository.findById(sellerPrincipal.getId()).orElseThrow(UserNotFoundException::new);
        sample.setSeller(seller);

        Perfume perfume = perfumeRepository.findById(dto.getPerfumeId()).orElseThrow(PerfumeNotFoundException::new);
        sample.setPerfume(perfume);

        return SampleMapper.toResponseDto(sampleRepository.save(sample));
    }

    @Override
    public SampleResponseDTO updateSample(Long id, SampleRequestDTO dto, User sellerPrincipal) {
        validateSampleRequestComplete(dto);

        Sample existing = findActiveByIdOrThrow(id);
        if (existing.getSeller() == null || !existing.getSeller().getId().equals(sellerPrincipal.getId())) {
            throw new SampleNotOwnedForUpdateException();
        }

        SampleMapper.applyFullUpdate(dto, existing);

        Perfume perfume = perfumeRepository.findById(dto.getPerfumeId()).orElseThrow(PerfumeNotFoundException::new);
        existing.setPerfume(perfume);

        return SampleMapper.toResponseDto(sampleRepository.save(existing));
    }

    @Override
    public SampleResponseDTO updateSampleStock(Long id, Integer newStock, User sellerPrincipal) {
        validateNewStock(newStock);
        Sample existing = findActiveByIdOrThrow(id);
        if (existing.getSeller() == null || !existing.getSeller().getId().equals(sellerPrincipal.getId())) {
            throw new SampleNotOwnedForUpdateException();
        }
        existing.setStock(newStock);
        return SampleMapper.toResponseDto(sampleRepository.save(existing));
    }

    @Override
    public void deleteSample(Long id, User principal) {
        Sample sample = sampleRepository.findById(id).orElseThrow(SampleNotFoundException::new);
        if (principal.getRole() == Role.SELLER) {
            if (sample.getSeller() == null || !sample.getSeller().getId().equals(principal.getId())) {
                throw new SampleNotOwnedForDeleteException();
            }
        }
        if (!sample.isActive()) {
            return;
        }
        sample.setActive(false);
        sampleRepository.save(sample);
    }
    
    private void validateSampleRequestComplete(SampleRequestDTO dto) {
        if (dto == null) {
            throw new SampleIncompleteRequestException();
        }
        boolean incomplete = dto.getPrice() == null
                || dto.getStock() == null
                || dto.getVolumeMl() == null
                || dto.getPerfumeId() == null
                || dto.getDescription() == null || dto.getDescription().isBlank()
                || dto.getImageUrl() == null || dto.getImageUrl().isBlank();
        if (incomplete) {
            throw new SampleIncompleteRequestException();
        }
    }

    private Sample findActiveByIdOrThrow(Long id) {
        Sample sample = sampleRepository.findById(id).orElseThrow(SampleNotFoundException::new);
        if (!sample.isActive()) {
            throw new SampleNotFoundException();
        }
        return sample;
    }

    private void validateNewStock(Integer newStock) {
        if (newStock == null || newStock < 0) {
            throw new StockUpdateInvalidException();
        }
    }
}