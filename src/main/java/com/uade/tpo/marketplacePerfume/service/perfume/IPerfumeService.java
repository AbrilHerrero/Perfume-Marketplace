package com.uade.tpo.marketplacePerfume.service.perfume;

import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs.PerfumeCreateDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs.PerfumeModifyDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs.PerfumeResponseDTO;
import com.uade.tpo.marketplacePerfume.exceptions.perfume.PerfumeNotFoundException;

public interface IPerfumeService {
    List<PerfumeResponseDTO> getPerfumes();
    String deletePerfume(Long id) throws PerfumeNotFoundException;
    PerfumeResponseDTO addPerfume(PerfumeCreateDTO perfumeCreateDTO);
    PerfumeResponseDTO modifyPerfume(Long id, PerfumeModifyDTO perfumeModifyDTO) throws PerfumeNotFoundException;
}
