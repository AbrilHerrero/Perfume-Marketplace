package com.uade.tpo.marketplacePerfume.controllers;

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

import com.uade.tpo.marketplacePerfume.entity.Sample;
import com.uade.tpo.marketplacePerfume.service.ISampleService;

@RestController // Define que esta clase es un controlador API Rest
@RequestMapping("/api/samples") // Ruta base para los endpoints de muestras
public class SampleController {

    @Autowired // Inyecta el servicio para usar la lógica de negocio
    private ISampleService sampleService;

    // 1. OBTENER TODAS LAS MUESTRAS (READ)
    // Acceso: GET http://localhost:8080/api/samples
    @GetMapping
    public List<Sample> getAll() {
        return sampleService.getAllSamples();
    }

    // 2. OBTENER UNA MUESTRA POR ID (READ)
    // Acceso: GET http://localhost:8080/api/samples/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Sample> getById(@PathVariable Long id) {
        // ResponseEntity ayuda a devolver códigos de estado HTTP (200 OK, 404 Not Found, etc.)
        return ResponseEntity.ok(sampleService.getSampleById(id));
    }

    // 3. CREAR UNA NUEVA MUESTRA (CREATE)
    // Acceso: POST http://localhost:8080/api/samples
    @PostMapping
    public ResponseEntity<Sample> create(@RequestBody Sample sample) {
        // @RequestBody convierte el JSON que viene del Front-end en un objeto Sample
        return ResponseEntity.ok(sampleService.createSample(sample));
    }

    // 4. ACTUALIZAR UNA MUESTRA (UPDATE)
    // Acceso: PUT http://localhost:8080/api/samples/{id}
    @PutMapping("/{id}")
    public ResponseEntity<Sample> update(@PathVariable Long id, @RequestBody Sample sample) {
        return ResponseEntity.ok(sampleService.updateSample(id, sample));
    }

    // 5. ELIMINAR UNA MUESTRA (DELETE)
    // Acceso: DELETE http://localhost:8080/api/samples/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        sampleService.deleteSample(id);
        // Devolvemos 204 No Content para indicar que se borró con éxito y no hay nada más que mostrar
        return ResponseEntity.noContent().build();
    }
}
