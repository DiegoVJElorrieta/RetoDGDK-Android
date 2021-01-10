package com.elorrieta.euskweatherapp;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ClientThread implements Runnable{

    private String resultado;

    public ClientThread(){}

    @Override
    public void run(){
        ResultSet rs = null;
        PreparedStatement st = null;
        Connection con = null;
        String IP, PUERTO, BBDD;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            IP = "172.16.10.31";
            PUERTO = "3306";
            BBDD = "prueba";
            String url = "jdbc:mysql://" + IP + ":" + PUERTO + "/" + BBDD + "?serverTimezone=UTC";
            con = DriverManager.getConnection(url, "root", "");
            String sql = "SELECT * FROM cliente";
            st = con.prepareStatement(sql);
            rs = st.executeQuery();

            while(rs.next()){
                String var = rs.getString("Nombre");
                Log.i("XXXXXXX", var);
                resultado = var;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public String getResponse(){
        return resultado;
    }

}
