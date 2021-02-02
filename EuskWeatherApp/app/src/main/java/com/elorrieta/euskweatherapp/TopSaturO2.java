package com.elorrieta.euskweatherapp;

public class TopSaturO2 {
    private String nomProv;
    private String nomMuni;
    private String SaturO2;

    public TopSaturO2() {
    }

    public TopSaturO2(String nomProv, String nomMuni, String saturO2) {
        this.nomProv = nomProv;
        this.nomMuni = nomMuni;
        this.SaturO2 = saturO2;
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

    public String getSaturO2() {
        return SaturO2;
    }

    public void setSaturO2(String saturO2) {
        SaturO2 = saturO2;
    }
}
