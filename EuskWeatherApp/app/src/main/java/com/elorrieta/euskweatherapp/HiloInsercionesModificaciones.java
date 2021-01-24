package com.elorrieta.euskweatherapp;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class HiloInsercionesModificaciones implements Runnable{

    private String nomApe, direccion, mail, nomUsu, contrasenia;

    public HiloInsercionesModificaciones(){}

    public HiloInsercionesModificaciones(String nomApe, String direccion, String mail, String nomUsu, String contrasenia){
        //CONSTRUCTOR PARA INSERCIONES DE USUARIOS
        this.nomApe = nomApe;
        this.direccion = direccion;
        this.mail = mail;
        this.nomUsu = nomUsu;
        this.contrasenia = contrasenia;
    }

    public HiloInsercionesModificaciones(String nomUsu, String contrasenia){
        this.nomUsu = nomUsu;
        this.contrasenia = contrasenia;
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
            sPuerto = "3306";
            sBBDD = "euskweather";
            String url = "jdbc:mysql://" + sIP + ":" + sPuerto + "/" + sBBDD + "?serverTimezone=UTC";
            con = DriverManager.getConnection(url, "root", "");// Consulta sencilla en este caso.
            if(MenuPrincipal.CAMBIO_CONTRA == true){
                sql = "UPDATE usuarios SET contrasenia='" + this.contrasenia + "' WHERE nickUsuario='" + this.nomUsu + "'";
            } else {
                sql = "INSERT INTO usuarios(nombreApellido, direccion, mail, nickUsuario, contrasenia) " +
                        "VALUES('" + this.nomApe + "', '" + this.direccion + "', '" + this.mail + "', '" + this.nomUsu + "', '" + this.contrasenia + "')";
            }
            st = con.prepareStatement(sql);
            st.executeUpdate();
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

}
