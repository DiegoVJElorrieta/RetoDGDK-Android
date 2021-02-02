package com.elorrieta.euskweatherapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class ListadoEspaciosNaturales extends AppCompatActivity {

    private Button btnPlayas, btnRios, btnPantanos;
    private RecyclerView recyclerViewEspaciosNaturales;
    private ArrayList<EspacioNatural> listaEspaciosNaturales;
    private ConnectivityManager connectivityManager = null;
    public static boolean CONSULTA_ESPACIO_NATURAL;
    public static String TIPO_ESPACIO = "";
    public static String nombreEspacioNatural;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_espacios_naturales);


        btnPlayas = (Button) findViewById(R.id.btnPlayas);
        btnRios = (Button) findViewById(R.id.btnRios);
        btnPantanos = (Button) findViewById(R.id.btnPantanos);
        recyclerViewEspaciosNaturales = (RecyclerView) findViewById(R.id.recyclerViewEspaciosNaturales);

        btnPlayas.setBackgroundColor(Color.rgb(28, 237, 253));
        btnRios.setBackgroundColor(Color.rgb(28, 237, 253));
        btnPantanos.setBackgroundColor(Color.rgb(28, 237, 253));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.menuopciones, m);
        MenuItem itemCerrarSesion = m.findItem(R.id.itemCerrarSesion);
        MenuItem itemCamara = m.findItem(R.id.camara);
        MenuItem itemCompartir = m.findItem(R.id.compartir);
        itemCompartir.setVisible(false);
        itemCerrarSesion.setVisible(false);
        itemCamara.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi){
        int id = mi.getItemId();
        if(id == R.id.itemAcercaDe){
            AlertDialog.Builder msj = new AlertDialog.Builder(this);
            msj.setTitle(R.string.tituloAcercaDe);
            msj.setMessage(R.string.descriAcercaDe);
            AlertDialog mostrarMensaje = msj.create();
            mostrarMensaje.show();
        }

        return super.onOptionsItemSelected(mi);
    }

    private ArrayList conectar() throws InterruptedException {
        ClientThread clientThread = new ClientThread(listaEspaciosNaturales);
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

    public void mostrarPlayas(View v){
        TIPO_ESPACIO = "Playas";
        btnPlayas.setBackgroundColor(Color.rgb(140, 105, 178));
        btnRios.setBackgroundColor(Color.rgb(28, 237, 253));
        btnPantanos.setBackgroundColor(Color.rgb(28, 237, 253));

        listaEspaciosNaturales = new ArrayList<>();
        try {
            if (isConnected()) {
                CONSULTA_ESPACIO_NATURAL = true;
                listaEspaciosNaturales.clear();
                listaEspaciosNaturales = conectar();
            } else {
                Toast.makeText(getApplicationContext(), "ERROR_NO_INTERNET", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException e) {// This cannot happen!
            Toast.makeText(getApplicationContext(), "ERROR_GENERAL", Toast.LENGTH_SHORT).show();
        }

        EspacioNaturalAdapter ena = new EspacioNaturalAdapter(listaEspaciosNaturales, new OnItemClickListener() {
            @Override
            public void onItemClick(Municipio item) {

            }

            @Override
            public void onItemClick(EspacioNatural item) {
                AlertDialog.Builder mensaje = new AlertDialog.Builder(ListadoEspaciosNaturales.this);
                mensaje.setTitle("INFO DE ESPACIO NATURAL");
                mensaje.setMessage("Nombre: " + item.getNombreEspacioNat());
                nombreEspacioNatural = item.getNombreEspacioNat();
                mensaje.setNegativeButton("CERRAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                mensaje.setPositiveButton("MOSTRAR INFORMACION", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(), InformacionEspacioNatural.class);
                        i.putExtra("nombreEspNat", item.getNombreEspacioNat());
                        i.putExtra("tipoEspNat", item.getTipo());
                        startActivity(i);
                    }
                });
                mensaje.show();
            }

            @Override
            public void onItemClick(Favoritos item) {

            }

            @Override
            public void onItemClick(Foto item) {

            }
        });
        recyclerViewEspaciosNaturales.setAdapter(ena);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewEspaciosNaturales.setLayoutManager(linearLayoutManager);
        CONSULTA_ESPACIO_NATURAL = false;
    }

    public void mostrarRios(View v){
        TIPO_ESPACIO = "Rios";
        btnRios.setBackgroundColor(Color.rgb(140, 105, 178));
        btnPlayas.setBackgroundColor(Color.rgb(28, 237, 253));
        btnPantanos.setBackgroundColor(Color.rgb(28, 237, 253));

        listaEspaciosNaturales = new ArrayList<>();
        try {
            if (isConnected()) {
                CONSULTA_ESPACIO_NATURAL = true;
                listaEspaciosNaturales.clear();
                listaEspaciosNaturales = conectar();
            } else {
                Toast.makeText(getApplicationContext(), "ERROR_NO_INTERNET", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException e) {// This cannot happen!
            Toast.makeText(getApplicationContext(), "ERROR_GENERAL", Toast.LENGTH_SHORT).show();
        }

        EspacioNaturalAdapter ena = new EspacioNaturalAdapter(listaEspaciosNaturales, new OnItemClickListener() {
            @Override
            public void onItemClick(Municipio item) {

            }

            @Override
            public void onItemClick(EspacioNatural item) {
                AlertDialog.Builder mensaje = new AlertDialog.Builder(ListadoEspaciosNaturales.this);
                mensaje.setTitle("INFO DE ESPACIO NATURAL");
                mensaje.setMessage("Nombre: " + item.getNombreEspacioNat());
                nombreEspacioNatural = item.getNombreEspacioNat();
                mensaje.setNegativeButton("CERRAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                mensaje.setPositiveButton("MOSTRAR INFORMACION", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(), InformacionEspacioNatural.class);
                        i.putExtra("nombreEspNat", item.getNombreEspacioNat());
                        i.putExtra("tipoEspNat", item.getTipo());
                        startActivity(i);
                    }
                });
                mensaje.show();
            }

            @Override
            public void onItemClick(Favoritos item) {

            }

            @Override
            public void onItemClick(Foto item) {

            }
        });
        recyclerViewEspaciosNaturales.setAdapter(ena);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewEspaciosNaturales.setLayoutManager(linearLayoutManager);
        CONSULTA_ESPACIO_NATURAL = false;
    }

    public void mostrarPantanos(View v){
        TIPO_ESPACIO = "Pantanos";
        btnPantanos.setBackgroundColor(Color.rgb(140, 105, 178));
        btnPlayas.setBackgroundColor(Color.rgb(28, 237, 253));
        btnRios.setBackgroundColor(Color.rgb(28, 237, 253));

        listaEspaciosNaturales = new ArrayList<>();
        try {
            if (isConnected()) {
                CONSULTA_ESPACIO_NATURAL = true;
                listaEspaciosNaturales.clear();
                listaEspaciosNaturales = conectar();
            } else {
                Toast.makeText(getApplicationContext(), "ERROR_NO_INTERNET", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException e) {// This cannot happen!
            Toast.makeText(getApplicationContext(), "ERROR_GENERAL", Toast.LENGTH_SHORT).show();
        }

        EspacioNaturalAdapter ena = new EspacioNaturalAdapter(listaEspaciosNaturales, new OnItemClickListener() {
            @Override
            public void onItemClick(Municipio item) {

            }

            @Override
            public void onItemClick(EspacioNatural item) {
                AlertDialog.Builder mensaje = new AlertDialog.Builder(ListadoEspaciosNaturales.this);
                mensaje.setTitle("INFO DE ESPACIO NATURAL");
                mensaje.setMessage("Nombre: " + item.getNombreEspacioNat());
                nombreEspacioNatural = item.getNombreEspacioNat();
                mensaje.setNegativeButton("CERRAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                mensaje.setPositiveButton("MOSTRAR INFORMACION", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(), InformacionEspacioNatural.class);
                        i.putExtra("nombreEspNat", item.getNombreEspacioNat());
                        i.putExtra("tipoEspNat", item.getTipo());
                        startActivity(i);
                    }
                });
                mensaje.show();
            }

            @Override
            public void onItemClick(Favoritos item) {

            }

            @Override
            public void onItemClick(Foto item) {

            }
        });
        recyclerViewEspaciosNaturales.setAdapter(ena);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewEspaciosNaturales.setLayoutManager(linearLayoutManager);
        CONSULTA_ESPACIO_NATURAL = false;
    }
}