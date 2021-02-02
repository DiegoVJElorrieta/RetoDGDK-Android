package com.elorrieta.euskweatherapp;

import android.icu.text.IDNA;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HiloInformacionMeteo implements Runnable{

    private ArrayList listado;
    private InformacionMeteorologica infMeteo;
    private String nombre;

    public HiloInformacionMeteo(){}

    public HiloInformacionMeteo(String nombre){
        this.nombre = nombre;
    }

    @Override
    public void run() {
        ResultSet rs = null;
        PreparedStatement st = null;
        Connection con = null;
        String sIP;
        String sPuerto;
        String sBBDD;
        try {
            Class.forName("com.mysql.jdbc.Driver");//Aqui pondriamos la IP y puerto.//sIP = "192.168.2.91";
            sIP = "192.168.13.252";
            sPuerto = "3306";
            sBBDD = "euskweather";
            String url = "jdbc:mysql://" + sIP + ":" + sPuerto + "/" + sBBDD + "?serverTimezone=UTC";
            con = DriverManager.getConnection(url, "root", "");// Consulta sencilla en este caso.
            String sql = "SELECT * FROM informacionMeteorologica WHERE nomEstMet in (select nombreEstacion from estacionMeteorologica where nomMunicipio='" + this.nombre + "')";
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            while(rs.next()){
               InformacionMunicipio.presionAtm = rs.getString("presionAtm");
                InformacionMunicipio.temperatura = rs.getString("temperatura");
                InformacionMunicipio.saturacion = rs.getInt("saturacionO2");
                InformacionMunicipio.calidadAire = rs.getString("calidadAire");
            }

        } catch (ClassNotFoundException e) {
            Log.e("ClassNotFoundException", "");
            e.printStackTrace();
        } catch (SQLException e) {
            Log.e("SQLException", "");
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("Exception", "");
            e.printStackTrace();
        } finally {// Intentamos cerrar _todo.
            try {// Cerrar ResultSet
                if (rs != null) {
                    rs.close();
                }// Cerrar PreparedStatement
                if (st != null) {
                    st.close();
                }// Cerrar Connection
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
                Log.e("Exception_cerrando todo", "");
                e.printStackTrace();
            }
        }
    }

    public ArrayList getResponse() {
        return listado;
    }

}
