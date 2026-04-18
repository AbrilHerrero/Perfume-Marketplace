package com.uade.tpo.marketplacePerfume.service.perfume;

import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs.PerfumeCreateDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs.PerfumeModifyDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs.PerfumeResponseDTO;

public interface IPerfumeService {
    List<PerfumeResponseDTO> getPerfumes();
    String deletePerfume(Long id);
    PerfumeResponseDTO addPerfume(PerfumeCreateDTO perfumeCreateDTO);
    PerfumeResponseDTO modifyPerfume(Long id, PerfumeModifyDTO perfumeModifyDTO);
}
