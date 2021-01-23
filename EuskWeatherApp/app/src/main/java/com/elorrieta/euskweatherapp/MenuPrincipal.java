package com.elorrieta.euskweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MenuPrincipal extends AppCompatActivity {

    private TextView txtBienvenido;
    public static boolean CAMBIO_CONTRA;
    Bundle datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        txtBienvenido = (TextView) findViewById(R.id.txtBienvenido);

        datos = getIntent().getExtras();
        if(datos!= null){
            txtBienvenido.setText(txtBienvenido.getText().toString() + ", " + datos.getString("nomUsername"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.menuopciones, m);
        MenuItem itemCambioContra = m.findItem(R.id.itemCambioPass);
        MenuItem itemCerrarSesion = m.findItem(R.id.itemCerrarSesion);
        MenuItem itemCamara = m.findItem(R.id.camara);
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
        if(id == R.id.itemCambioPass){
            CAMBIO_CONTRA = true;
            Intent i = new Intent(this, RegistroUsuario.class);
            i.putExtra("usuarioCambio", datos.getString("nomUsername"));
            startActivity(i);
        }

        return super.onOptionsItemSelected(mi);
    }

    @Override
    public void onBackPressed(){
        Toast.makeText(this, R.string.salirMenu, Toast.LENGTH_SHORT).show();
    }

    public void listadoMunicipios(View v){
        Intent i = new Intent(this, ListadoMunicipios.class);
        startActivity(i);
    }

    public void listadoEspaciosNaturales(View v){
        Intent i = new Intent(this, ListadoEspaciosNaturales.class);
        startActivity(i);
    }


    public void cerrarSesion(View v){
        MainActivity.EXISTE_USUARIO = false;
        Toast.makeText(this, R.string.cierreSesionCorrecto, Toast.LENGTH_LONG).show();
        finish();
    }

}