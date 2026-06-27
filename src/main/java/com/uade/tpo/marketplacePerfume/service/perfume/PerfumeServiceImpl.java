package com.uade.tpo.marketplacePerfume.service.perfume;

import java.time.Year;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplacePerfume.entity.Perfume;
import com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs.PerfumeCreateDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs.PerfumeModifyDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs.PerfumeResponseDTO;
import com.uade.tpo.marketplacePerfume.exceptions.perfume.PerfumeInvalidYearException;
import com.uade.tpo.marketplacePerfume.exceptions.perfume.PerfumeNotFoundException;
import com.uade.tpo.marketplacePerfume.mapper.PerfumeMapper;
import com.uade.tpo.marketplacePerfume.repository.PerfumeRepository;

@Service
public class PerfumeServiceImpl implements IPerfumeService {

    @Autowired
    private PerfumeRepository perfumeRepository;

    private static final int MIN_RELEASE_YEAR = 1900;

    @Override
    public List<PerfumeResponseDTO> getPerfumes() {
        return PerfumeMapper.toResponseDtoList(perfumeRepository.findAll());
    }

    @Override
    public PerfumeResponseDTO getPerfume(Long id) {
        Perfume perfume = perfumeRepository.findById(id)
                .orElseThrow(PerfumeNotFoundException::new);
        return PerfumeMapper.toResponseDto(perfume);
    }

    @Override
    public String deletePerfume(Long id) {
        if (!perfumeRepository.existsById(id)) {
            throw new PerfumeNotFoundException();
        }
        perfumeRepository.deleteById(id);
        return "Deleted successfully";
    }

    @Override
    public PerfumeResponseDTO addPerfume(PerfumeCreateDTO perfumeCreateDTO) {
        validateReleaseYear(perfumeCreateDTO.getReleaseYear());
        Perfume perfume = PerfumeMapper.toEntityFromCreate(perfumeCreateDTO);
        Perfume saved = perfumeRepository.save(perfume);
        return PerfumeMapper.toResponseDto(saved);
    }

    @Override
    public PerfumeResponseDTO modifyPerfume(Long id, PerfumeModifyDTO perfumeModifyDTO) {
        validateReleaseYear(perfumeModifyDTO.getReleaseYear());
        Perfume existing = perfumeRepository.findById(id)
                .orElseThrow(PerfumeNotFoundException::new);
        PerfumeMapper.applyModify(perfumeModifyDTO, existing);
        Perfume updated = perfumeRepository.save(existing);
        return PerfumeMapper.toResponseDto(updated);
    }

    private void validateReleaseYear(Integer releaseYear) {
        if (releaseYear == null) {
            return;
        }
        if (releaseYear < MIN_RELEASE_YEAR || releaseYear > Year.now().getValue()) {
            throw new PerfumeInvalidYearException();
        }
    }
}
