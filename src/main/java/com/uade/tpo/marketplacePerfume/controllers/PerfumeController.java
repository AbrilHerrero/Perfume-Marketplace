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

import com.uade.tpo.marketplacePerfume.entity.dto.PerfumeDTO;
import com.uade.tpo.marketplacePerfume.exceptions.PerfumeNotFoundException;
import com.uade.tpo.marketplacePerfume.service.IPerfumeService;

@RestController
@RequestMapping("perfume")
public class PerfumeController {

    @Autowired
    private IPerfumeService perfumeService;

    @GetMapping("/all")
    public ResponseEntity<List<PerfumeDTO>> getPerfumes() {
        return ResponseEntity.ok(perfumeService.getPerfumes());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deletePerfume(@PathVariable Long id) throws PerfumeNotFoundException {
        return ResponseEntity.ok(perfumeService.deletePerfume(id));
    }

    @PostMapping
    public ResponseEntity<PerfumeDTO> addPerfume(@RequestBody PerfumeDTO perfumeDTO) {
        PerfumeDTO created = perfumeService.addPerfume(perfumeDTO);
        return ResponseEntity.created(URI.create("/perfume/" + created.getId())).body(created);
    }

    @PutMapping("{id}")
    public ResponseEntity<PerfumeDTO> modifyPerfume(@PathVariable Long id, @RequestBody PerfumeDTO perfumeDTO) throws PerfumeNotFoundException {
        return ResponseEntity.ok(perfumeService.modifyPerfume(id, perfumeDTO));
    }
}
