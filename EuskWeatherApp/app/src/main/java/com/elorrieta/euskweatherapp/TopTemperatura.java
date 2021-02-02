package com.elorrieta.euskweatherapp;

public class TopTemperatura {
    private String nomProv;
    private String nomMuni;
    private String temperatura;

    public TopTemperatura() {
    }

    public TopTemperatura(String nomProv, String nomMuni, String temperatura) {
        this.nomProv = nomProv;
        this.nomMuni = nomMuni;
        this.temperatura = temperatura;
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

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }
}
