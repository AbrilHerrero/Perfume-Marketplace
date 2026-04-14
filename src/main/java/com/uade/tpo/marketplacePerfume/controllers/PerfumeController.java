package com.uade.tpo.marketplacePerfume.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs.PerfumeCreateDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs.PerfumeModifyDTO;
import com.uade.tpo.marketplacePerfume.entity.dto.perfumeDTOs.PerfumeResponseDTO;
import com.uade.tpo.marketplacePerfume.exceptions.perfume.PerfumeNotFoundException;
import com.uade.tpo.marketplacePerfume.service.perfume.IPerfumeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("perfume")
public class PerfumeController {

    @Autowired
    private IPerfumeService perfumeService;

    @GetMapping("/all")
    public ResponseEntity<List<PerfumeResponseDTO>> getPerfumes() {
        return ResponseEntity.ok(perfumeService.getPerfumes());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deletePerfume(@PathVariable Long id) throws PerfumeNotFoundException {
        return ResponseEntity.ok(perfumeService.deletePerfume(id));
    }

    @PostMapping
    public ResponseEntity<PerfumeResponseDTO> addPerfume(@Valid @RequestBody PerfumeCreateDTO perfumeCreateDTO) {
        PerfumeResponseDTO created = perfumeService.addPerfume(perfumeCreateDTO);
        return ResponseEntity.created(URI.create("/perfume/" + created.getId())).body(created);
    }

    @PutMapping("{id}")
    public ResponseEntity<PerfumeResponseDTO> modifyPerfume(@PathVariable Long id, @RequestBody PerfumeModifyDTO perfumeModifyDTO) throws PerfumeNotFoundException {
        return ResponseEntity.ok(perfumeService.modifyPerfume(id, perfumeModifyDTO));
    }
}
