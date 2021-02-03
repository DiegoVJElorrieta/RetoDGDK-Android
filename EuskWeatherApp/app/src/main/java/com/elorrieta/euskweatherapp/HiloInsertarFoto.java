package com.elorrieta.euskweatherapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HiloInsertarFoto implements Runnable{

    private String foto;
    private String nomMuni;

    public HiloInsertarFoto() {
    }

    public HiloInsertarFoto(String foto, String nomMuni) {
        this.foto = foto;
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
            sIP = "192.168.1.83";
            //sIP = "192.168.13.252";
            sPuerto = "3306";
            sBBDD = "euskweather";
            String url = "jdbc:mysql://" + sIP + ":" + sPuerto + "/" + sBBDD + "?serverTimezone=UTC";
            con = DriverManager.getConnection(url, "root", "");// Consulta sencilla en este caso.
            sql = "INSERT INTO fotos (fotoString, nombreMunicipio) " +
                    "VALUES('" + this.foto + "', '" + this.nomMuni + "')";

            st = con.prepareStatement(sql);
            st.executeUpdate();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
