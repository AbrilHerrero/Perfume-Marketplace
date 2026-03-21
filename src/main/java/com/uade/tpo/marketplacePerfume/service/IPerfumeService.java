package com.uade.tpo.marketplacePerfume.service;
import java.util.ArrayList;

import com.uade.tpo.marketplacePerfume.entity.dto.PerfumeDTO;
import com.uade.tpo.marketplacePerfume.exceptions.PerfumeNotFoundException;

public interface IPerfumeService {
    public ArrayList<PerfumeDTO> getPerfumes ();
    public String deletePerfume (int id) throws PerfumeNotFoundException;
    public PerfumeDTO addPerfume (int id, String name, String email, String password, String telephone);
    public PerfumeDTO modifyPerfume ();
}