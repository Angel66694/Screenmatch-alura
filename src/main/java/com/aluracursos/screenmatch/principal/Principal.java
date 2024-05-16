package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.DatosSerie;
import com.aluracursos.screenmatch.model.DatosTemporadas;
import com.aluracursos.screenmatch.model.Serie;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=4fc7c187";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosSerie> datosSeries = new ArrayList<>();

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1)- Buscar series
                    1)- Buscar episodios
                    3)- Mostrar series buscadas
                                        
                    0)- Salir
                    """;

            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSeriesBuscadas();
                    break;
                case 0:
                    System.out.println("Grasias por visitar mi paguina vuelve pronto");
                default:
                    System.out.println("Obcion no valida");
                    break;
            }

        }
    }



    private DatosSerie getDatoSerie(){
        System.out.println("Escribe el nombre de la seerie que buscas ");
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        System.out.println(json);
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }

    private  void buscarEpisodioPorSerie(){
        DatosSerie datosSerie = getDatoSerie();
        List<DatosTemporadas> temporadas = new ArrayList<>();

        for (int i = 1; i <= datosSerie.totalDeTemporadas() ; i++) {
            var json = consumoApi.obtenerDatos(URL_BASE + datosSerie.titulo().replace(" ", "+")+ API_KEY);
            DatosTemporadas datosTemporadas = conversor.obtenerDatos(json, DatosTemporadas.class);
            temporadas.add(datosTemporadas);
        }
        temporadas.forEach(System.out::println);
    }

    private void buscarSerieWeb() {
        DatosSerie datos = getDatoSerie();
        datosSeries.add(datos);
        System.out.println(datos);

    }
    private void mostrarSeriesBuscadas() {
        List<Serie> series = new ArrayList<>();
        series = datosSeries.stream()
                .map(d -> {
                    try {
                        return new Serie(d);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);

    }
}





