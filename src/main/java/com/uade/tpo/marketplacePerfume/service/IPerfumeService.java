package com.uade.tpo.marketplacePerfume.service;
import java.util.ArrayList;

import com.uade.tpo.marketplacePerfume.entity.Perfume;
import com.uade.tpo.marketplacePerfume.exceptions.PerfumeNotFoundException;

public interface IPerfumeService {
    public ArrayList<Perfume> getPerfumes ();
    public String deletePerfume (int id) throws PerfumeNotFoundException;
    public Perfume addPerfume (int id, String name, String email, String password, String telephone);
    public Perfume modifyPerfume ();
}