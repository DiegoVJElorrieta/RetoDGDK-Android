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
    private ArrayList listado;
    private Municipio m;
    private EspacioNatural en;
    private Favoritos fav;

    public ClientThread(){}

    public ClientThread(ArrayList listado) {
        this.listado = listado;
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
            if(ListadoMunicipios.CONSULTA_MUNICIPIOS == true){
                String sql = "SELECT * FROM municipios WHERE idProvincia=" + ListadoMunicipios.idProvincia;
                st = con.prepareStatement(sql);
                rs = st.executeQuery();
                while(rs.next()){
                    String nomMuni = rs.getString("nombreMuni");
                    String alcaldeMuni = rs.getString("alcalde");
                    String webMuni = rs.getString("webMunicipio");
                    m = new Municipio();
                    m.setNombreMuni(nomMuni);
                    m.setAlcaldeMuni(alcaldeMuni);
                    m.setWebMuni(webMuni);
                    listado.add(m);
                }
            } else if(ListadoEspaciosNaturales.CONSULTA_ESPACIO_NATURAL == true){
                String sql = "SELECT * FROM espaciosnaturales WHERE tipoEspNat='" + ListadoEspaciosNaturales.TIPO_ESPACIO + "'";
                st = con.prepareStatement(sql);
                rs = st.executeQuery();
                while(rs.next()){
                    String nomEspNat = rs.getString("nombreEspNat");
                    String descri = rs.getString("descripcion");
                    String tipo = rs.getString("tipoEspNat");
                    en = new EspacioNatural();
                    en.setNombreEspacioNat(nomEspNat);
                    en.setDescripcion(descri);
                    en.setTipo(tipo);
                    listado.add(en);
                }
            } else if(ListadoFavoritos.CONSULTA_FAVORITOS == true) {
                String sql = "select * from favoritos";
                st = con.prepareStatement(sql);
                rs = st.executeQuery();
                while(rs.next()){
                    String nombreMuni = rs.getString("nomMuni");
                    fav = new Favoritos();
                    fav.setNombreMuni(nombreMuni);

                    listado.add(fav);
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
        return listado;
    }
}
