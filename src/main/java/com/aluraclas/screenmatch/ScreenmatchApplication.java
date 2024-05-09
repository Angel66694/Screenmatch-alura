package com.aluraclas.screenmatch;

import com.aluraclas.screenmatch.Principal.EjemploStreams;
import com.aluraclas.screenmatch.Principal.Principal;
import com.aluraclas.screenmatch.model.DatosEpisodio;
import com.aluraclas.screenmatch.model.DatosSerie;
import com.aluraclas.screenmatch.model.DatosTemporadads;
import com.aluraclas.screenmatch.service.ConsumoAPI;
import com.aluraclas.screenmatch.service.ConvierteDatos;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {
	

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		/*Principal principal = new Principal();
		principal.muestraElMenu();*/
		EjemploStreams ejemploStreams = new EjemploStreams();
		ejemploStreams.muestraEjemplo();



	}}
