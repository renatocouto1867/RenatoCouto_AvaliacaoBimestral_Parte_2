package com.example.renatocouto_avaliacaobimestral_parte_2.entity;

public class Pokemon {
    private final int id;
    private final String name;
    private final String url;
    private final String imagemUrl;
    private final String habilidade;
    private final int pontos;

    public Pokemon(int id, String name, String url, String imagemUrl, String hablidade) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.imagemUrl = imagemUrl;
        this.habilidade = hablidade;
        this.pontos = calcularPontos(hablidade);
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getImagemUrl() {
        return imagemUrl;
    }

    public String getHabilidade() {
        return habilidade;
    }

    public int getPontos() {
        return pontos;
    }

    public int calcularPontos(String texto) {
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
