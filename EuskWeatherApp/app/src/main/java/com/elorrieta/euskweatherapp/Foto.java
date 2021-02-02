package com.elorrieta.euskweatherapp;

public class Foto {

    private String fotoString;
    private String nombreMunicipio;

    public Foto() {
    }

    public Foto(String fotoString, String nombreMunicipio) {
        this.fotoString = fotoString;
        this.nombreMunicipio = nombreMunicipio;
    }

    public String getFotoString() {
        return fotoString;
    }

    public void setFotoString(String fotoString) {
        this.fotoString = fotoString;
    }

    public String getNombreMunicipio() {
        return nombreMunicipio;
    }

    public void setNombreMunicipio(String nombreMunicipio) {
        this.nombreMunicipio = nombreMunicipio;
    }
}
