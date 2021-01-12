package com.elorrieta.euskweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.List;

public class MenuPrincipal extends AppCompatActivity {

    Bundle datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
    }

    @Override
    public void onBackPressed(){
        Toast.makeText(this, R.string.salirMenu, Toast.LENGTH_SHORT).show();
    }

    public void listadoMunicipios(View v){
        Intent i = new Intent(this, ListadoMunicipios.class);
        startActivity(i);
    }

    public void cerrarSesion(View v){
        MainActivity.EXISTE_USUARIO = false;
        Toast.makeText(this, R.string.cierreSesionCorrecto, Toast.LENGTH_LONG).show();
        finish();
    }

}