package com.uade.tpo.marketplacePerfume.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplacePerfume.entity.Perfume;
import com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs.PerfumeCreateDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs.PerfumeModifyDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs.PerfumeResponseDTO;
import com.uade.tpo.marketplacePerfume.exceptions.PerfumeNotFoundException;
import com.uade.tpo.marketplacePerfume.mapper.PerfumeMapper;
import com.uade.tpo.marketplacePerfume.repository.PerfumeRepository;

@Service
public class PerfumeServiceImpl implements IPerfumeService {

    @Autowired
    private PerfumeRepository perfumeRepository;

    @Override
    public List<PerfumeResponseDTO> getPerfumes() {
        return PerfumeMapper.toResponseDtoList(perfumeRepository.findAll());
    }

    @Override
    public String deletePerfume(Long id) {
        if (!perfumeRepository.existsById(id)) {
            throw new PerfumeNotFoundException();
        }
        perfumeRepository.deleteById(id);
        return "Borrado correctamente";
    }

    @Override
    public PerfumeResponseDTO addPerfume(PerfumeCreateDTO perfumeCreateDTO) {
        Perfume perfume = PerfumeMapper.toEntityFromCreate(perfumeCreateDTO);
        Perfume saved = perfumeRepository.save(perfume);
        return PerfumeMapper.toResponseDto(saved);
    }

    @Override
    public PerfumeResponseDTO modifyPerfume(Long id, PerfumeModifyDTO perfumeModifyDTO) {
        Perfume existing = perfumeRepository.findById(id)
                .orElseThrow(PerfumeNotFoundException::new);
        PerfumeMapper.applyModify(perfumeModifyDTO, existing);
        Perfume updated = perfumeRepository.save(existing);
        return PerfumeMapper.toResponseDto(updated);
    }
}
