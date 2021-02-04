package com.elorrieta.euskweatherapp;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HiloInfoMeteoEspNat implements Runnable{

    private String nomMuni;
    private ArrayList listaInfoMetEsp;
    private InformacionMeteorologica infMeteo;

    public HiloInfoMeteoEspNat(){

    }

    public HiloInfoMeteoEspNat(ArrayList listaInfoMetEsp, String nomMuni){
        this.listaInfoMetEsp = listaInfoMetEsp;
        this.nomMuni = nomMuni;
    }

    @Override
    public void run() {
        ResultSet rs = null;
        PreparedStatement st = null;
        Connection con = null;
        String sIP;
        String sql;
        String sPuerto;
        String sBBDD;
        try {
            Class.forName("com.mysql.jdbc.Driver");//Aqui pondriamos la IP y puerto.//sIP = "192.168.2.91";
            //sIP = "192.168.1.83";
            sIP = "192.168.13.252";
            sPuerto = "3306";
            sBBDD = "euskweather";
            String url = "jdbc:mysql://" + sIP + ":" + sPuerto + "/" + sBBDD + "?serverTimezone=UTC";
            con = DriverManager.getConnection(url, "root", "");// Consulta sencilla en este caso.
            sql = "SELECT * FROM informacionmeteorologica where nomEstMet in " +
                    "(select nombreEstacion from estacionmeteorologica where nomMunicipio in " +
                    "(select nomMunicipio from espaciosnaturales where nombreEspNat ='" + this.nomMuni + "'))";

            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            while(rs.next()){
                String temperatura = rs.getString("temperatura");
                String calidadAire = rs.getString("calidadAire");
                //Log.e("TEMPERATURA", "TEMP " + this.nomMuni + " :" + temperatura);
                if(temperatura != null && calidadAire != null) {
                    infMeteo = new InformacionMeteorologica();
                    infMeteo.setTemperatura(temperatura);
                    infMeteo.setCalidadAire(calidadAire);
                    listaInfoMetEsp.add(infMeteo);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
