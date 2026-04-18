package com.uade.tpo.marketplacePerfume.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.uade.tpo.marketplacePerfume.entity.Perfume;
import com.uade.tpo.marketplacePerfume.entity.Sample;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.Sample.SampleRequestDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.Sample.SampleResponseDTO;
import com.uade.tpo.marketplacePerfume.exceptions.PerfumeNonExistanceException;
import com.uade.tpo.marketplacePerfume.exceptions.SampleNotFoundException;
import com.uade.tpo.marketplacePerfume.exceptions.UserNonExistanceException;
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
        return SampleMapper.toResponseDtoList(
            sampleRepository.findAll().stream().filter(Sample::isActive).collect(Collectors.toList())
        );
    }

    @Override
    public List<SampleResponseDTO> getSamplesBySellerId(Long sellerId) {
        return SampleMapper.toResponseDtoList(sampleRepository.findBySellerIdAndActiveTrue(sellerId));
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
        if (dto.getPerfumeId() == null) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "perfumeId is required when creating a sample");
        }

        Sample sample = SampleMapper.toEntityFromRequest(dto);
        sample.setCreatedAt(LocalDateTime.now());

        User seller = userRepository.findById(sellerPrincipal.getId()).orElseThrow(UserNonExistanceException::new);
        sample.setSeller(seller);

        Perfume perfume = perfumeRepository.findById(dto.getPerfumeId()).orElseThrow(PerfumeNonExistanceException::new);
        sample.setPerfume(perfume);

        return SampleMapper.toResponseDto(sampleRepository.save(sample));
    }

    @Override
    public SampleResponseDTO updateSample(Long id, SampleRequestDTO dto, User sellerPrincipal) {
        Sample existing = findActiveByIdOrThrow(id);
        if (existing.getSeller() == null || !existing.getSeller().getId().equals(sellerPrincipal.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only update your own samples");
        }

        SampleMapper.applyModify(dto, existing);

        if (dto.getPerfumeId() != null) {
            Perfume perfume = perfumeRepository.findById(dto.getPerfumeId()).orElseThrow(PerfumeNonExistanceException::new);
            existing.setPerfume(perfume);
        }

        return SampleMapper.toResponseDto(sampleRepository.save(existing));
    }

    @Override
    public void deleteSample(Long id) {
        Sample sample = sampleRepository.findById(id).orElseThrow(() -> new SampleNotFoundException(id));
        if (!sample.isActive()) {
            return;
        }
        sample.setActive(false);
        sampleRepository.save(sample);
    }

    private Sample findActiveByIdOrThrow(Long id) {
        Sample sample = sampleRepository.findById(id).orElseThrow(() -> new SampleNotFoundException(id));
        if (!sample.isActive()) {
            throw new SampleNotFoundException(id);
        }
        return sample;
    }
}