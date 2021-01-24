package com.elorrieta.euskweatherapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import java.util.ArrayList;

public class ListadoFavoritos extends AppCompatActivity {

    public static boolean CONSULTA_FAVORITOS;
    public static int id;
    private ConnectivityManager connectivityManager = null;
    private ArrayList<Favoritos> listaFavoritos;
    private RecyclerView recyclerViewFavoritos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_favoritos);
        recyclerViewFavoritos = (RecyclerView) findViewById(R.id.recyclerViewFavoritos);

        listaFavoritos = new ArrayList<>();

        try {
            if(isConnected()) {
                CONSULTA_FAVORITOS = true;
                listaFavoritos.clear();
                listaFavoritos = conectar();
            }else {
                Toast.makeText(getApplicationContext(), "ERROR_NO_INTERNET", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException e) {// This cannot happen!
            Toast.makeText(getApplicationContext(), "ERROR_GENERAL", Toast.LENGTH_SHORT).show();
        }
        FavoritosAdapter fav = new FavoritosAdapter(listaFavoritos, new OnItemClickListener() {
            @Override
            public void onItemClick(Municipio item) {
                AlertDialog.Builder mensaje = new AlertDialog.Builder(ListadoFavoritos.this);
                mensaje.setTitle("INFO DE MUNICIPIO");
                mensaje.setMessage("Nombre: " + item.getNombreMuni() + "\n" +
                        "Alcalde: " + item.getAlcaldeMuni() + "\n" +
                        "Pagina web: " + item.getWebMuni());
            }

            @Override
            public void onItemClick(EspacioNatural item) {

            }

            @Override
            public void onItemClick(Favoritos item) {

            }
        });
        recyclerViewFavoritos.setAdapter(fav);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewFavoritos.setLayoutManager(linearLayoutManager);
        CONSULTA_FAVORITOS = false;
    }

    private ArrayList conectar() throws InterruptedException {
        ClientThread clientThread = new ClientThread(listaFavoritos);
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join(); // Esperar respusta del servidor...
        return clientThread.getResponse();
    }

    public boolean isConnected() {
        boolean ret = false;
        try {
            connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if ((networkInfo != null) && (networkInfo.isAvailable()) && (networkInfo.isConnected()))
                ret = true;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error_comunicación", Toast.LENGTH_SHORT).show();
        }
        return ret;
    }
}