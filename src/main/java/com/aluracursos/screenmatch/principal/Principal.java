package com.aluracursos.screenmatch.principal;

import com.aluracursos.screenmatch.model.*;
import com.aluracursos.screenmatch.repository.SerieRepository;
import com.aluracursos.screenmatch.service.ConsumoAPI;
import com.aluracursos.screenmatch.service.ConvierteDatos;
import org.springframework.dao.DataIntegrityViolationException;
import com.aluracursos.screenmatch.Traductor.ConsultaDeepL;

import java.util.*;
import java.util.stream.Collectors;


public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=4fc7c187";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosSerie> datosSeries = new ArrayList<>();
    private  SerieRepository repositorio;
    private  List<Serie> series;

    public Principal(SerieRepository repository) {
        this.repositorio = repository;
    }


    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1)- Buscar series
                    2)- Buscar episodios
                    3)- Mostrar series buscadas
                    4)- Buscar series por titulo
                    5)- Top 5 mejores series
                    6)- Buescar series por categoria
                    7)- filtrar series por cantidad de temporadas y evaluacion
                    8)- Buscar episodio por nombre
                    
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
                case 4:
                    buscarSeriesPorTitulo();
                    break;
                case 5:
                    buscarTop5Series();
                    break;
                case 6:
                    buscarSeriesPorCategoria();
                    break;
                case 7:
                    buscarSeriePorNumeroDeTemporadaYEvaluacion();
                    break;
                case 8:
                    buscarEpisodiosPorTitulo();
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
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }

    private  void buscarEpisodioPorSerie(){
        mostrarSeriesBuscadas();
        System.out.println("Escribe el nombre de la cual quieres ver los episodios");
        var nombreSerie = teclado.nextLine();

        Optional <Serie> serie = series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst();

        if (serie.isPresent()){
            var serieEncontrada = serie.get();
            List<DatosTemporadas> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalDeTemporadas() ; i++) {
                var json = consumoApi.obtenerDatos(URL_BASE +
                        serieEncontrada.getTitulo().replace(" ", "+")+
                        "&season="+ i + API_KEY);
                DatosTemporadas datosTemporadas = conversor.obtenerDatos
                        (json, DatosTemporadas.class);
                temporadas.add(datosTemporadas);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);

        }
        }


    private void buscarSerieWeb() {
        try {
            DatosSerie datos = getDatoSerie();
            Serie serie = new Serie(datos);
            String traducccionTexto = ConsultaDeepL.obtenerTraduccion(serie.getReseña());
           // System.out.println(traducccionTexto);
            serie.setReseña(traducccionTexto);
            repositorio.save(serie);

            System.out.println(datos);
        }catch (DataIntegrityViolationException e){
            System.out.println("Error serie ya buscada");
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void mostrarSeriesBuscadas() {
        series = repositorio.findAll();

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero,
                        Comparator.nullsLast(Comparator.naturalOrder())))
                .forEach(System.out::println);

    }

    private void buscarSeriesPorTitulo(){
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        Optional<Serie> serieBuscada = repositorio.findByTituloContainingIgnoreCase
                (nombreSerie);

        if (serieBuscada.isPresent()){
            System.out.println("La serie buscada es: " + serieBuscada.get());
        }else {
            System.out.println("Serie no encontrada");
        }

    }
    private void  buscarTop5Series(){
        List<Serie> topSeries = repositorio.findTop5ByOrderByEvaluacionDesc();
        topSeries.forEach(s -> System.out.println("Serie: " + s.getTitulo()
                + "Evaluacion: " + s.getEvaluacion()));
    }
    private void buscarSeriesPorCategoria()  {
        System.out.println("Escribe el genero de la serie que deseas buscar");
        var genero = teclado.nextLine();
        var categoria = Categoria.fromEspanol(genero);
        List<Serie> seriePorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Las series de la categoria " + genero);
        seriePorCategoria.forEach(System.out::println);
    }

    private void buscarSeriePorNumeroDeTemporadaYEvaluacion(){
        System.out.println("Filtrar serie con cuantas temporadas? ");
        var numeroTemporadas = teclado.nextInt();
        teclado.nextLine();
        System.out.println("Con evaluacion apartir de cual valor? ");
        var evaluacion = teclado.nextDouble();
        teclado.nextLine();
        List<Serie> filtroSeries = repositorio.seriesPorTemporadaYEvaluacion(numeroTemporadas, evaluacion);
        System.out.println("** Series filtradas **");
        filtroSeries.forEach(s ->
                System.out.println(s.getTitulo() + " - evaluacion: " + s.getEvaluacion()));

    }
    private void buscarEpisodiosPorTitulo(){
        System.out.println("Escribe el nombre del episodio que deseas busacar");
        var nombreEpisodio = teclado.nextLine();
        List<Episodio> episodiosEncontrados = repositorio.episodiosPorNombre(nombreEpisodio);
        episodiosEncontrados.forEach(e ->
                System.out.printf("Serie: %s Temporada: %d Episodio: %d Evaluación: %.2f\n",
                        e.getSerie().getTitulo(), e.getTemporada(), e.getNumeroEpisodio(), e.getEvaluacion()));
    }

}






