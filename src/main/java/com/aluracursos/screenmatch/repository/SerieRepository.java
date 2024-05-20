package com.aluracursos.screenmatch.repository;
import com.aluracursos.screenmatch.model.Categoria;
import com.aluracursos.screenmatch.model.Episodio;
import com.aluracursos.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie,Long> {
    Optional<Serie> findByTituloContainingIgnoreCase(String nombreSerie);

    List<Serie> findTop5ByOrderByEvaluacionDesc();
    List<Serie> findByGenero(Categoria categoria);

    @Query("SELECT s FROM Serie s WHERE s.totalDeTemporadas >= :totalDeTemporadas AND s.evaluacion >= :evaluacion")
    List<Serie> seriesPorTemporadaYEvaluacion(int totalDeTemporadas, Double evaluacion);


    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE LOWER(e.titulo) LIKE LOWER(CONCAT('%', :nombreEpisodio, '%'))")
    List<Episodio> episodiosPorNombre(String nombreEpisodio);


}
