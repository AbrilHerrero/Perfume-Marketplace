package com.uade.tpo.marketplacePerfume.service;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import com.uade.tpo.marketplacePerfume.entity.Perfume;
import com.uade.tpo.marketplacePerfume.repository.PerfumeRepository;
import com.uade.tpo.marketplacePerfume.exceptions.PerfumeNotFoundException;

@Service
public class PerfumeServiceImpl implements IPerfumeService   {
    private PerfumeRepository perfumeRepository;
   
    public PerfumeServiceImpl (){
        perfumeRepository=new PerfumeRepository();
    }

    @Override
    public ArrayList<Perfume> getPerfumes() {
        return perfumeRepository.getPerfumes();
    }

    @Override
    public String deletePerfume(int id) throws PerfumeNotFoundException {

        boolean exists = perfumeRepository.getPerfumes().stream().anyMatch(p -> p.getId() == id);
    
        if (!exists) {
            throw new PerfumeNotFoundException();
        }
    
        perfumeRepository.deletePerfume(id);
        String message ="Borrado correctaente";
        return message;
    }

    

    @Override
    public Perfume addPerfume(int id, String name, String email, String password, String telephone) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addPerfume'");
    }

    @Override
    public Perfume modifyPerfume() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'modifyPerfume'");
    }
    
}
