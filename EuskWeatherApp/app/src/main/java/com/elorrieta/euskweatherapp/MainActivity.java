package com.elorrieta.euskweatherapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText txtUsuario, txtPassword;
    private ConnectivityManager connectivityManager = null;
    public static boolean EXISTE_USUARIO;
    public static String usuarioApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.menuopciones, m);
        MenuItem itemCambioContra = m.findItem(R.id.itemCambioPass);
        itemCambioContra.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem mi){
        int id = mi.getItemId();
        if(id == R.id.itemAcercaDe){
            AlertDialog.Builder msj = new AlertDialog.Builder(this);
            msj.setTitle("Acerca de...");
            msj.setMessage("App desarrollada por EuskWeather Group");
            AlertDialog mostrarMensaje = msj.create();
            mostrarMensaje.show();
        }

        return super.onOptionsItemSelected(mi);
    }

    public void conexionOnClick(View v){
        try{
            if(isConnected()){
                String respuesta = conectar();
                if(null == respuesta){
                    Toast.makeText(getApplicationContext(), "ERROR EN LA COMUNICACION", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(this, "CONECTADO", Toast.LENGTH_SHORT).show();
                }
            } else{
                Toast.makeText(getApplicationContext(), "NO INTERNET", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException e){
            Toast.makeText(getApplicationContext(), "ERROR_GENERAL", Toast.LENGTH_SHORT).show();
        }
    }

    private String conectar() throws InterruptedException {
        ClientThread clientThread = new ClientThread();
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join();

        return clientThread.getResponse();
    }

    public boolean isConnected(){
        boolean ret = false;
        try{
            connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if((networkInfo != null) && (networkInfo.isAvailable()) && (networkInfo.isConnected())){
                ret = true;
            }
        } catch(Exception e){
            Toast.makeText(getApplicationContext(), "Error_comunicación", Toast.LENGTH_SHORT).show();
        }
        return ret;
    }

    public void btnLogin(View v){
        validarRegistro(v);
        if(EXISTE_USUARIO == true){
            Intent i = new Intent(this, MenuPrincipal.class);
            i.putExtra("nomUsername", txtUsuario.getText().toString());
            startActivity(i);
        }
    }

    public void btnRegistro(View v){
        SharedPreferences users = getSharedPreferences("usuarios", Context.MODE_PRIVATE);
        String usuario = txtUsuario.getText().toString(), password = txtPassword.getText().toString();
        String user = users.getString(usuario, "");
        if(user.length() == 0){
            Intent i = new Intent(this, RegistroUsuario.class);
            i.putExtra("nomUsu", txtUsuario.getText().toString());
            startActivity(i);
        } else{
            Toast.makeText(this, "EL USUARIO YA EXISTE", Toast.LENGTH_SHORT).show();
        }
    }

    public void validarRegistro(View v){
        String usuario = txtUsuario.getText().toString();
        usuarioApp = usuario;
        String password = txtPassword.getText().toString();
        SharedPreferences users = getSharedPreferences("usuarios", Context.MODE_PRIVATE);
        String user = users.getString(usuario, "");
        if(user.length() == 0){
            Toast.makeText(this, "NO VALIDO", Toast.LENGTH_LONG).show();
            EXISTE_USUARIO = false;
        } else{
            if(password.equals(user)){
                Toast.makeText(this, "INICIO EXITOSO", Toast.LENGTH_LONG).show();
                EXISTE_USUARIO = true;
            } else{
                Toast.makeText(this, "CONTRASEÑA INCORRECTA", Toast.LENGTH_LONG).show();
            }
        }
    }
}