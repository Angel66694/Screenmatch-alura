package com.aluraclas.screenmatch.model;

import java.time.DateTimeException;
import java.time.LocalDate;

public class Episodio {
    private  Integer temporada;
    private String titulo;
    private  Integer numeroDeEpisodio;
    private Double evaluacion;
     private LocalDate fechaDeLanzamiento;

    public Episodio(Integer numero, DatosEpisodio d) {
        this.temporada = numero;
        this.numeroDeEpisodio = d.numeroEpisodio();
        this.titulo = d.titulo();

        try {
            this.evaluacion = Double.valueOf(d.evaluacion());
        }catch (NumberFormatException e){
            this.evaluacion = 0.0;
        }
        try {
            this.fechaDeLanzamiento = LocalDate.parse(d.fechaDeLanzamiento());
        }catch (DateTimeException e){
            this.fechaDeLanzamiento = null;
        }


    }

    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getNumeroDeEpisodio() {
        return numeroDeEpisodio;
    }

    public void setNumeroDeEpisodio(Integer numeroDeEpisodio) {
        this.numeroDeEpisodio = numeroDeEpisodio;
    }

    public Double getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Double evaluacion) {
        this.evaluacion = evaluacion;
    }

    public LocalDate getFechaDeLanzamiento() {
        return fechaDeLanzamiento;
    }

    public void setFechaDeLanzamiento(LocalDate fechaDeLanzamiento) {
        this.fechaDeLanzamiento = fechaDeLanzamiento;
    }

    @Override
    public String toString() {
        return
                "temporada=" + temporada +
                        ", numeroDeEpisodio=" + numeroDeEpisodio +
                ", titulo='" + titulo + '\'' +
                ", evaluacion=" + evaluacion +
                ", fechaDeLanzamiento=" + fechaDeLanzamiento;

    }
}
