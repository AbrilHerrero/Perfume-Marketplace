package com.uade.tpo.marketplacePerfume.repository;

import java.util.ArrayList;
import java.util.Optional;

import com.uade.tpo.marketplacePerfume.entity.Perfume;

public class PerfumeRepository {
    private ArrayList<Perfume> perfumes;
        
    public PerfumeRepository() {

        perfumes = new ArrayList<>();

        perfumes.add(
                Perfume.builder()
                        .id(1)
                        .name("Sauvage")
                        .brand("Dior")
                        .line("Sauvage")
                        .description("Fragancia fresca y especiada")
                        .releaseYear(2015)
                        .build()
        );

        perfumes.add(
                Perfume.builder()
                        .id(2)
                        .name("Bleu de Chanel")
                        .brand("Chanel")
                        .line("Bleu")
                        .description("Perfume amaderado aromático")
                        .releaseYear(2010)
                        .build()
        );

        perfumes.add(
                Perfume.builder()
                        .id(3)
                        .name("Acqua di Gio")
                        .brand("Armani")
                        .line("Acqua di Gio")
                        .description("Fragancia fresca marina")
                        .releaseYear(1996)
                        .build()
        );

        perfumes.add(
                Perfume.builder()
                        .id(4)
                        .name("La Vie Est Belle")
                        .brand("Lancôme")
                        .line("La Vie Est Belle")
                        .description("Perfume dulce floral")
                        .releaseYear(2012)
                        .build()
        );

        perfumes.add(
                Perfume.builder()
                        .id(5)
                        .name("Black Opium")
                        .brand("Yves Saint Laurent")
                        .line("Opium")
                        .description("Fragancia dulce con café y vainilla")
                        .releaseYear(2014)
                        .build()
        );
    }

    public ArrayList <Perfume> getPerfumes (){
        return this.perfumes;
    }

    public Perfume addPerfume ( int id, String name, String brand, String line, String description, int releaseYear){
        Perfume newPerfume = Perfume.builder().id(id).name(name).brand(brand).line(line).description(description).releaseYear(releaseYear).build();
        perfumes.add(newPerfume);
        return newPerfume;
    }

    public void deletePerfume(int id) {
        perfumes.removeIf(perfume -> perfume.getId() == id);  
    }

    public Optional<Perfume> getPerfumeById (int id){
        return this.perfumes.stream().filter(m->m.getId() == id).findAny();
    }

    
}
