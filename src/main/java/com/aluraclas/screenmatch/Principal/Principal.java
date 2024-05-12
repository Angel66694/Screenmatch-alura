package com.aluraclas.screenmatch.Principal;

import com.aluraclas.screenmatch.model.DatosEpisodio;
import com.aluraclas.screenmatch.model.DatosSerie;
import com.aluraclas.screenmatch.model.DatosTemporadads;
import com.aluraclas.screenmatch.model.Episodio;
import com.aluraclas.screenmatch.service.ConsumoAPI;
import com.aluraclas.screenmatch.service.ConvierteDatos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

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
      //  temporadads.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        //convertir todas las informaciones a una lista datosEpisodio
        List<DatosEpisodio> datosEpisodios = temporadads.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());



        //top 5 episoios
        System.out.println("Top 5 episodios");
        datosEpisodios.stream()
                .filter(e -> !e.evaluacion().equalsIgnoreCase("N/A"))
                .peek(e -> System.out.println("Primer filtro (N/A)" + e))
                .sorted(Comparator.comparing(DatosEpisodio::evaluacion).reversed())
                .peek(e -> System.out.println("segundo filtro ordenacion (M>m)" + e))
                .map(e-> e.titulo().toUpperCase())
                .peek(e -> System.out.println("tercer filtro mayuscula (m>M)" + e))
                .limit(5)
                .forEach(System.out::println);



        //convirtiendo los datos a una lista del tipo episodio

      /*  List<Episodio> episodios = temporadads.stream()
                .flatMap(t ->t.episodios().stream()
                        .map(d -> new Episodio(t.numero(),d)))
                .collect(Collectors.toList());

                episodios.forEach(System.out::println);*/

                //Busqueda de episodios por fecha
        System.out.println("Ingresa el año del capitulo que deseas buscar ");
        var fecha = teclado.nextInt();
        teclado.nextLine();

        LocalDate fehcaBusqueda = LocalDate.of(fecha, 1,1);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyy");

     /*   episodios.stream()
                .filter(e -> e.getFechaDeLanzamiento() != null && e.getFechaDeLanzamiento().isAfter(fehcaBusqueda))
                .forEach(e -> System.out.println(
                        "Temporasa " + e.getTemporada()+
                                "Episodio " + e.getTitulo()+
                                "fecha de lanzamiento " + e.getFechaDeLanzamiento().format(dtf)*/

        //***************** Busac episodio por parte del titulo*********************

        System.out.println("Favor de ingresr parte del titutlo que desea buscar");
        var busauqedaTitulo = teclado.nextInt();
        teclado.nextLine();

        datosEpisodios.stream()
                //.filter()
                .findFirst();





    }
    }
