package com.elorrieta.euskweatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class TopProvincias extends AppCompatActivity {
    public static boolean Consulta_Saturacion;
    public static boolean Consulta_Presion;
    public static boolean Consulta_Temperatura;
    public static String Provincia;

    public Spinner spinnerProv;
    private ArrayList<TopSaturO2> saturO2List;
    private ArrayList<TopPresionATM> presionATMList;
    private ArrayList<TopTemperatura> tempList;

    private RecyclerView recyclerViewTopSatur;
    private ConnectivityManager connectivityManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_provincias);

        String[] arraySpinner = new String[] {
                "Araba/Álava", "Bizkaia", "Gipuzkoa"
        };
        spinnerProv = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProv.setAdapter(adapter);


    }

    public void saturacionO2(View v){
        recyclerViewTopSatur = (RecyclerView) findViewById(R.id.recyclerViewTop);

        saturO2List = new ArrayList<>();
        Provincia = spinnerProv.getSelectedItem().toString();
        try {
            if(isConnected()) {
                Consulta_Saturacion = true;
                saturO2List.clear();
                saturO2List = conectar(saturO2List);
            }else {
                Toast.makeText(getApplicationContext(), "ERROR_NO_INTERNET", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException e) {// This cannot happen!
            Toast.makeText(getApplicationContext(), "ERROR_GENERAL", Toast.LENGTH_SHORT).show();
        }
        TopSaturO2Adapter topO2 = new TopSaturO2Adapter(saturO2List);
        recyclerViewTopSatur.setAdapter(topO2);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewTopSatur.setLayoutManager(linearLayoutManager);
        Provincia = null;
        Consulta_Saturacion = false;

    }

    public void presion(View v) {
        recyclerViewTopSatur = (RecyclerView) findViewById(R.id.recyclerViewTop);

        presionATMList = new ArrayList<>();

        Provincia = spinnerProv.getSelectedItem().toString();
        try {
            if(isConnected()) {
                Consulta_Presion = true;
                presionATMList.clear();
                presionATMList = conectar(presionATMList);
            }else {
                Toast.makeText(getApplicationContext(), "ERROR_NO_INTERNET", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException e) {// This cannot happen!
            Toast.makeText(getApplicationContext(), "ERROR_GENERAL", Toast.LENGTH_SHORT).show();
        }
        TopPresionATMAdapter topATM = new TopPresionATMAdapter(presionATMList);
        recyclerViewTopSatur.setAdapter(topATM);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewTopSatur.setLayoutManager(linearLayoutManager);
        Provincia = null;
        Consulta_Presion = false;
    }

    public void temperatura(View v) {
        recyclerViewTopSatur = (RecyclerView) findViewById(R.id.recyclerViewTop);

        tempList = new ArrayList<>();

        Provincia = spinnerProv.getSelectedItem().toString();
        try {
            if(isConnected()) {
                Consulta_Temperatura = true;
                tempList.clear();
                tempList = conectar(tempList);
            }else {
                Toast.makeText(getApplicationContext(), "ERROR_NO_INTERNET", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException e) {// This cannot happen!
            Toast.makeText(getApplicationContext(), "ERROR_GENERAL", Toast.LENGTH_SHORT).show();
        }
        TopTemperaturaAdapter topTemp = new TopTemperaturaAdapter(tempList);
        recyclerViewTopSatur.setAdapter(topTemp);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewTopSatur.setLayoutManager(linearLayoutManager);
        Provincia = null;
        Consulta_Temperatura = false;
    }

    private ArrayList conectar(ArrayList ar) throws InterruptedException {
        ClientThread clientThread = new ClientThread(ar);
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