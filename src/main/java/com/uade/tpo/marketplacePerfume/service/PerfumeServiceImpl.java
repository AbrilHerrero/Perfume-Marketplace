package com.uade.tpo.marketplacePerfume.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplacePerfume.entity.Perfume;
import com.uade.tpo.marketplacePerfume.entity.dto.PerfumeDTO;
import com.uade.tpo.marketplacePerfume.exceptions.PerfumeNotFoundException;
import com.uade.tpo.marketplacePerfume.mapper.PerfumeMapper;
import com.uade.tpo.marketplacePerfume.repository.PerfumeRepository;

@Service
public class PerfumeServiceImpl implements IPerfumeService {

    @Autowired
    private PerfumeRepository perfumeRepository;

    @Override
    public List<PerfumeDTO> getPerfumes() {
        return PerfumeMapper.toDtoList(perfumeRepository.findAll());
    }

    @Override
    public String deletePerfume(Long id) throws PerfumeNotFoundException {
        if (!perfumeRepository.existsById(id)) {
            throw new PerfumeNotFoundException();
        }
        perfumeRepository.deleteById(id);
        return "Borrado correctamente";
    }

    @Override
    public PerfumeDTO addPerfume(PerfumeDTO perfumeDTO) {
        Perfume perfume = PerfumeMapper.toEntity(perfumeDTO);
        perfume.setId(null);
        Perfume saved = perfumeRepository.save(perfume);
        return PerfumeMapper.toDto(saved);
    }

    @Override
    public PerfumeDTO modifyPerfume(Long id, PerfumeDTO perfumeDTO) throws PerfumeNotFoundException {
        Perfume existing = perfumeRepository.findById(id)
                .orElseThrow(PerfumeNotFoundException::new);
        existing.setName(perfumeDTO.getName());
        existing.setBrand(perfumeDTO.getBrand());
        existing.setLine(perfumeDTO.getLine());
        existing.setDescription(perfumeDTO.getDescription());
        existing.setReleaseYear(perfumeDTO.getReleaseYear());
        Perfume updated = perfumeRepository.save(existing);
        return PerfumeMapper.toDto(updated);
    }
}
