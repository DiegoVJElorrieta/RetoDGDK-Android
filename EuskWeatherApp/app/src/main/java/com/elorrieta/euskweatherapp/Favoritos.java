package com.elorrieta.euskweatherapp;

public class Favoritos {
    private int idFav;
    private String nombreMuni;

    public Favoritos(){

    }

    public Favoritos(int idFav, String nombreMuni){
        this.idFav = idFav;
        this.nombreMuni = nombreMuni;
    }

    public int getIdFav() {
        return idFav;
    }

    public String getNombreMuni() {
        return nombreMuni;
    }

    public void setIdFav(int idFav) {
        this.idFav = idFav;
    }

    public void setNombreMuni(String nombreMuni) {
        this.nombreMuni = nombreMuni;
    }
}
