package com.elorrieta.euskweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static boolean EXISTE_USUARIO;
    private ConnectivityManager connectivityManager = null;
    private EditText txtUsuario, txtPassword;
    public static String usuarioApp;
    private ImageView logo;
    public static boolean INSERCION;

    private ObjectAnimator animacionAlphaAparecer;
    private ObjectAnimator animacionAlphaDesaparecer;
    private ObjectAnimator animacionRotar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        animaciones();
        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
    }

    private void animaciones(){
        logo = findViewById(R.id.imageView2);

        animacionAlphaAparecer = ObjectAnimator.ofFloat(logo, View.ALPHA, 1.0f,0.0f);
        animacionAlphaAparecer.setDuration(2500);
        animacionRotar = ObjectAnimator.ofFloat(logo, "rotation", 0f,360f);
        animacionRotar.setDuration(2500);

        AnimatorSet animatorSetTodo = new AnimatorSet();
        animatorSetTodo.playTogether(animacionAlphaAparecer, animacionRotar);
        animatorSetTodo.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                animacionAlphaDesaparecer = ObjectAnimator.ofFloat(logo, View.ALPHA, 0.0f,1.0f);
                animacionAlphaDesaparecer.setDuration(2500);
                animacionRotar = ObjectAnimator.ofFloat(logo, "rotation", 0f,360f);
                animacionRotar.setDuration(2500);

                AnimatorSet animatorSetTodo = new AnimatorSet();
                animatorSetTodo.playTogether(animacionAlphaDesaparecer, animacionRotar);
                animatorSetTodo.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        animaciones();
                    }
                });
                animatorSetTodo.start();
            }
        });
        animatorSetTodo.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.menuopciones, m);
        MenuItem itemCambioContra = m.findItem(R.id.itemCambioPass);
        MenuItem itemCerrarSesion = m.findItem(R.id.itemCerrarSesion);
        itemCambioContra.setVisible(false);
        itemCerrarSesion.setVisible(false);
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
            Toast.makeText(this, R.string.usuarioExiste, Toast.LENGTH_SHORT).show();
        }
    }

    public void validarRegistro(View v){
        String usuario = txtUsuario.getText().toString();
        usuarioApp = usuario;
        String password = txtPassword.getText().toString();
        SharedPreferences users = getSharedPreferences("usuarios", Context.MODE_PRIVATE);
        String user = users.getString(usuario, "");
        if(user.length() == 0){
            Toast.makeText(this, R.string.usuarioNoValido, Toast.LENGTH_LONG).show();
            EXISTE_USUARIO = false;
        } else{
            if(password.equals(user)){
                Toast.makeText(this, R.string.inicioSesionCorrecto, Toast.LENGTH_LONG).show();
                EXISTE_USUARIO = true;
            } else{
                Toast.makeText(this, R.string.contraIncorrecta, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void conectar() throws InterruptedException {
        HiloInsercionesModificaciones hilo = new HiloInsercionesModificaciones();
        Thread thread = new Thread(hilo);
        thread.start();
        thread.join();
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

    public void ingresarUsuario(View v){
        try {
            if (isConnected()) {
                conectar();
            } else {
                Toast.makeText(getApplicationContext(), "ERROR_NO_INTERNET", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {// This cannot happen!
            Toast.makeText(getApplicationContext(), "ERROR_GENERAL", Toast.LENGTH_SHORT).show();
        }
    }

}