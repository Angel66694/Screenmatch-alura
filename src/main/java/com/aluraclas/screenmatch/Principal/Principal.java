package com.aluraclas.screenmatch.Principal;

import com.aluraclas.screenmatch.model.DatosEpisodio;
import com.aluraclas.screenmatch.model.DatosSerie;
import com.aluraclas.screenmatch.model.DatosTemporadads;
import com.aluraclas.screenmatch.service.ConsumoAPI;
import com.aluraclas.screenmatch.service.ConvierteDatos;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new  ConsumoAPI();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final  String API_KEY = "&apikey=cf02dc87&";
    private ConvierteDatos conversor = new ConvierteDatos();

    public void muestraElMenu(){
        System.out.println("Porfavor escribe el nombre de la serie que deseas buscar");
        //Busca los datos generales de las series
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(
                URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        var datos = conversor.obtenerDatos(json, DatosSerie.class);
        System.out.println(datos);


        //Busca los dato de todas las temporadas
        List<DatosTemporadads> temporadads = new ArrayList<>();
        for (int i = 1; i <= datos.totalDeTemporadas() ; i++){
            json= consumoApi.obtenerDatos(
                    URL_BASE + nombreSerie.replace(" ", "+") + "&season="+i+API_KEY);
            var datosTemporadas = conversor.obtenerDatos(json, DatosTemporadads.class);
            temporadads.add(datosTemporadas);

        }
        //temporadads.forEach(System.out::println);

        //Mostrar solo el título de los episodios para las temporadas
        temporadads.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));


    }
}
