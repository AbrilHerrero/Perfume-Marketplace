package com.uade.tpo.marketplacePerfume.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplacePerfume.entity.Perfume;
import com.uade.tpo.marketplacePerfume.exceptions.PerfumeNotFoundException;
import com.uade.tpo.marketplacePerfume.service.PefumeService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
@RequestMapping("perfume")
public class PerfumeController {
    @Autowired
    private PefumeService perfumeService;

    @GetMapping()
    public ResponseEntity<ArrayList<Perfume>> getPerfuemes() {
        return ResponseEntity.ok(perfumeService.getPerfumes());
    }
    
    @DeleteMapping("{id}")
    public ResponseEntity<String> deletePerfume(@PathVariable int id) throws PerfumeNotFoundException {
        return ResponseEntity.ok(perfumeService.deletePerfume(id));
    }
}
