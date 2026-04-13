package com.uade.tpo.marketplacePerfume.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.marketplacePerfume.entity.Sample;
import com.uade.tpo.marketplacePerfume.repository.SampleRepository;

@Service // Indica a Spring que esta clase contiene la lógica de negocio (Service Layer)
public class SampleServiceImpl implements ISampleService {

    @Autowired // Inyección de dependencia: Spring nos da una instancia del repositorio automáticamente
    private SampleRepository sampleRepository;

    @Override
    public List<Sample> getAllSamples() {
        // Retorna todas las muestras almacenadas en la base de datos
        return sampleRepository.findAll();
    }

    @Override
    public Sample getSampleById(Long id) {
        // Intenta buscar la muestra por ID. Si no existe, lanza una excepción de error.
        return sampleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró la muestra con ID: " + id));
    }

    @Override
    public Sample createSample(Sample sample) {
        // Guarda la nueva muestra recibida en la BD
        return sampleRepository.save(sample);
    }

    @Override
    public Sample updateSample(Long id, Sample sampleDetails) {
        // 1. Buscamos la muestra que queremos editar para asegurarnos de que existe
        Sample sample = getSampleById(id); 
        
        // 2. Actualizamos los atributos con los datos nuevos que vienen en 'sampleDetails'
        // Usamos los nombres exactos de tu clase Sample.java
        sample.setPrice(sampleDetails.getPrice()); // BigDecimal
        sample.setVolumeMl(sampleDetails.getVolumeMl()); // int
        sample.setStock(sampleDetails.getStock()); // int
        sample.setDescription(sampleDetails.getDescription()); // String
        sample.setImageUrl(sampleDetails.getImageUrl()); // String
        
        // 3. Guardamos la entidad actualizada en la base de datos
        return sampleRepository.save(sample);
    }

    @Override
    public void deleteSample(Long id) {
        // Verificamos existencia y luego eliminamos el registro de la BD
        Sample sample = getSampleById(id);
        sampleRepository.delete(sample);
    }
}