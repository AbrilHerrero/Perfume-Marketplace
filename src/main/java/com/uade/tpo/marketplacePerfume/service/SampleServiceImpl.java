package com.uade.tpo.marketplacePerfume.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplacePerfume.entity.Perfume;
import com.uade.tpo.marketplacePerfume.entity.Sample;
import com.uade.tpo.marketplacePerfume.entity.User;
import com.uade.tpo.marketplacePerfume.entity.dto.Sample.SampleRequestDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.Sample.SampleResponseDTO;
import com.uade.tpo.marketplacePerfume.exceptions.SampleNotFoundException;
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
        return SampleMapper.toResponseDto(getSampleById(id));
    }

    @Override
    public Sample getSampleById(Long id) {
        return sampleRepository.findById(id).orElseThrow(() -> new SampleNotFoundException(id));
    }

    @Override
    public SampleResponseDTO createSample(SampleRequestDTO dto) {
        Sample sample = SampleMapper.toEntityFromRequest(dto);

        User seller = userRepository.findById(dto.getSellerId())
            .orElseThrow(() -> new RuntimeException("Seller no encontrado con id: " + dto.getSellerId()));
        sample.setSeller(seller);

        Perfume perfume = perfumeRepository.findById(dto.getPerfumeId())
            .orElseThrow(() -> new RuntimeException("Perfume no encontrado con id: " + dto.getPerfumeId()));
        sample.setPerfume(perfume);

        return SampleMapper.toResponseDto(sampleRepository.save(sample));
    }

    @Override
    public SampleResponseDTO updateSample(Long id, SampleRequestDTO dto) {
        Sample existing = getSampleById(id);
        SampleMapper.applyModify(dto, existing);

        if (dto.getSellerId() != null) {
            User seller = userRepository.findById(dto.getSellerId())
                .orElseThrow(() -> new RuntimeException("Seller no encontrado con id: " + dto.getSellerId()));
            existing.setSeller(seller);
        }

        if (dto.getPerfumeId() != null) {
            Perfume perfume = perfumeRepository.findById(dto.getPerfumeId())
                .orElseThrow(() -> new RuntimeException("Perfume no encontrado con id: " + dto.getPerfumeId()));
            existing.setPerfume(perfume);
        }

        return SampleMapper.toResponseDto(sampleRepository.save(existing));
    }

    @Override
    public void deleteSample(Long id) {
        Sample sample = getSampleById(id);
        sample.setActive(false);
        sampleRepository.save(sample);
    }
}