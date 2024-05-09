package com.aluraclas.screenmatch.Principal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EjemploStreams {
    public void muestraEjemplo(){
        List<String> nombres = Arrays.asList("Brenda","luis","Maria fernanda","Eric","Genesys");

        nombres.stream()
                .sorted()
                .limit(4)
                .forEach(System.out::println);
    }
}
