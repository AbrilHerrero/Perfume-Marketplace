package com.uade.tpo.marketplacePerfume.service;

import java.util.List;

import com.uade.tpo.marketplacePerfume.entity.Sample;

public interface ISampleService {
    
    // Método para obtener todas las muestras del catálogo
    List<Sample> getAllSamples();

    // Método para buscar una muestra específica por su ID
    Sample getSampleById(Long id);

    // Método para que un vendedor cree una nueva publicación de muestra
    Sample createSample(Sample sample);

    // Método para actualizar precio, stock o descripción
    Sample updateSample(Long id, Sample sampleDetails);

    // Método para dar de baja una muestra
    void deleteSample(Long id);
}
