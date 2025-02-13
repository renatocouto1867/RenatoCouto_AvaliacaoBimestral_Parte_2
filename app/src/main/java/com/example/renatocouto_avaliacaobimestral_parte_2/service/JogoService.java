package com.example.renatocouto_avaliacaobimestral_parte_2.service;

public class JogoService {
    public static int calcularPontos(String texto) {
        int pontos = 0;
        String vogais = "aeiouAEIOU";

        for (int i = 0; i < texto.length(); i++) {
            char caractere = texto.charAt(i);
            if (Character.isLetter(caractere)) {
                if (vogais.indexOf(caractere) != -1) {
                    pontos += 10;
                } else {
                    pontos += 15;
                }
            }
        }

        return pontos;
    }

}
