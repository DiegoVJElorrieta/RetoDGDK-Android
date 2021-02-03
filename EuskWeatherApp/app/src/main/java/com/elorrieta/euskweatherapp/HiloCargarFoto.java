package com.elorrieta.euskweatherapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HiloCargarFoto extends Thread{

    private String nomMuni;
    private Foto foto;
    private ArrayList<Foto> lista;

    public HiloCargarFoto() {
    }

    public HiloCargarFoto(ArrayList<Foto> lista, String nomMuni) {
        this.lista = lista;
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
            sql = "SELECT * FROM fotos WHERE nombreMunicipio = '" + this.nomMuni + "'";
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            String fotoCod = "", nomMunicipio ="";
            while(rs.next()){
                fotoCod = rs.getString("fotoString");
                nomMunicipio = rs.getString("nombreMunicipio");
               foto = new Foto();
                foto.setFotoString(fotoCod);
                foto.setNombreMunicipio(nomMunicipio);
                lista.add(foto);
            }
            InformacionMunicipio.fotoString = fotoCod;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
