package com.uade.tpo.marketplacePerfume.service;

import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.dto.PerfumeDTO;
import com.uade.tpo.marketplacePerfume.exceptions.PerfumeNotFoundException;

public interface IPerfumeService {
    List<PerfumeDTO> getPerfumes();
    String deletePerfume(Long id) throws PerfumeNotFoundException;
    PerfumeDTO addPerfume(PerfumeDTO perfumeDTO);
    PerfumeDTO modifyPerfume(Long id, PerfumeDTO perfumeDTO) throws PerfumeNotFoundException;
}
