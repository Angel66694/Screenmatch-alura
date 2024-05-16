package com.aluracursos.screenmatch.service;

public class Traducion {
    public static void main(String[] args) {
        try {
            String texto = "Hello, how are you?";
            String traduccion = ConsultaDeepL.obtenerTraduccion(texto);
            System.out.println("Traducción: " + traduccion);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
