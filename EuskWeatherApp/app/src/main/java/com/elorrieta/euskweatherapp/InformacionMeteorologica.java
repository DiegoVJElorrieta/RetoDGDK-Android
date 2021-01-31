package com.elorrieta.euskweatherapp;

public class InformacionMeteorologica {

    private int idInfo;
    private String fecha;
    private String hora;
    private String presionAtm;
    private String temperatura;
    private int saturacionO2;
    private String calidadAire;
    private String nomEstMet;

    public InformacionMeteorologica() {
    }

    public InformacionMeteorologica(int idInfo, String fecha, String hora, String presionAtm,
                                    String temperatura, int saturacionO2, String calidadAire, String nomEstMet) {
        this.idInfo = idInfo;
        this.fecha = fecha;
        this.hora = hora;
        this.presionAtm = presionAtm;
        this.temperatura = temperatura;
        this.saturacionO2 = saturacionO2;
        this.calidadAire = calidadAire;
        this.nomEstMet = nomEstMet;
    }

    public int getIdInfo() {
        return idInfo;
    }

    public void setIdInfo(int idInfo) {
        this.idInfo = idInfo;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getPresionAtm() {
        return presionAtm;
    }

    public void setPresionAtm(String presionAtm) {
        this.presionAtm = presionAtm;
    }

    public String getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(String temperatura) {
        this.temperatura = temperatura;
    }

    public int getSaturacionO2() {
        return saturacionO2;
    }

    public void setSaturacionO2(int saturacionO2) {
        this.saturacionO2 = saturacionO2;
    }

    public String getCalidadAire() {
        return calidadAire;
    }

    public void setCalidadAire(String calidadAire) {
        this.calidadAire = calidadAire;
    }

    public String getNomEstMet() {
        return nomEstMet;
    }

    public void setNomEstMet(String nomEstMet) {
        this.nomEstMet = nomEstMet;
    }
}
