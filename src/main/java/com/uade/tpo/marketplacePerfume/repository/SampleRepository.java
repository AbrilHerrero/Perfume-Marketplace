package com.uade.tpo.marketplacePerfume.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.uade.tpo.marketplacePerfume.entity.Sample;

@Repository
<<<<<<< HEAD
//Se cambia de class a interface porque JpaRepository ya tiene implementaciones de los métodos básicos
public interface SampleRepository extends JpaRepository<Sample, Long> {
    // Se pone sample y long porque el id de sample es de tipo long, y se heredan los métodos básicos de JpaRepository
    // Por ahora no escribo nada
}
=======
public interface SampleRepository extends JpaRepository<Sample, Long> {
}
>>>>>>> origin/main
