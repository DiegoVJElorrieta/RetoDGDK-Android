package com.elorrieta.euskweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MenuPrincipal extends AppCompatActivity {

    Bundle datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
    }

    @Override
    public void onBackPressed(){
        Toast.makeText(this, "DEBES CERRAR SESION", Toast.LENGTH_SHORT).show();
    }

    public void listadoMunicipios(View v){

    }

    public void cerrarSesion(View v){
        MainActivity.EXISTE_USUARIO = false;
        Toast.makeText(this, "SESION CERRADA CORRECTAMENTE", Toast.LENGTH_LONG).show();
        finish();
    }

}