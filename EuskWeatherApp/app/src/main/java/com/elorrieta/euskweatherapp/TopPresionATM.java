package com.elorrieta.euskweatherapp;

public class TopPresionATM {
    private String nomProv;
    private String nomMuni;
    private String Presion;

    public TopPresionATM() {
    }

    public TopPresionATM(String nomProv, String nomMuni, String presion) {
        this.nomProv = nomProv;
        this.nomMuni = nomMuni;
        this.Presion = presion;
    }

    public String getNomProv() {
        return nomProv;
    }

    public void setNomProv(String nomProv) {
        this.nomProv = nomProv;
    }

    public String getNomMuni() {
        return nomMuni;
    }

    public void setNomMuni(String nomMuni) {
        this.nomMuni = nomMuni;
    }

    public String getPresion() {
        return Presion;
    }

    public void setPresion(String presion) {
        Presion = presion;
    }
}
