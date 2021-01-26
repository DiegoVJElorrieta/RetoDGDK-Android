package com.elorrieta.euskweatherapp;

import android.util.Log;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HiloComprobarUsuario implements Runnable{

    private String usuario, contrasenia;

    public HiloComprobarUsuario(){}

    public HiloComprobarUsuario(String usuario, String contrasenia){
        this.usuario = usuario;
        this.contrasenia = contrasenia;
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
            String sql = "SELECT nickUsuario, contrasenia FROM usuarios WHERE nickUsuario='" + this.usuario + "'";
            st = con.prepareStatement(sql);
            rs = st.executeQuery();
            while(rs.next()){
                String nomUsu = rs.getString("nickUsuario");
                String contrasenia = rs.getString("contrasenia");
                if(this.usuario.equals(nomUsu) && this.contrasenia.equals(contrasenia)){
                    MainActivity.EXISTE_USUARIO = true;
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

}
