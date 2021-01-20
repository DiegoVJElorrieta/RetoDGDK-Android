package com.elorrieta.euskweatherapp;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientThread implements Runnable {
    private String sResultado;
    private ArrayList<String> listaMunicipios;

    public ClientThread(){}

    public ClientThread(ArrayList<String> listaMunicipios) {
        this.listaMunicipios = listaMunicipios;
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
            if(MainActivity.INSERCION == true){
                String sql = "INSERT INTO usuarios VALUES(2, 'prueba2', 'bbbb', 'adios@h.com', 'username2', 'reqr')";
                st = con.prepareStatement(sql);
                st.executeUpdate();
            }else if(ListadoMunicipios.CONSULTA_MUNICIPIOS == true){
                String sql = "SELECT * FROM `municipios` where idProvincia=" + ListadoMunicipios.idProvincia;
                st = con.prepareStatement(sql);
                rs = st.executeQuery();//--
                while (rs.next()) {
                    String var1 = rs.getString("nombreMuni");
                    Log.i("XXXXXXX", var1);
                    sResultado = var1;
                    listaMunicipios.add(sResultado);
                }
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
        return listaMunicipios;
    }
}
