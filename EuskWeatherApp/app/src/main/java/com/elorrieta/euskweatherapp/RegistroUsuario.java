package com.elorrieta.euskweatherapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistroUsuario extends AppCompatActivity {

    private EditText txtNomApe, txtDireccion, txtCorreo, txtNomUsuario, txtContra, txtContraConfirm;
    private TextView lblNomApe, lblDireccion, lblCorreo;
    private Button btnRegistrar;
    Bundle datos;
    private ConnectivityManager connectivityManager = null;
    public static String s, sa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        txtNomApe = (EditText) findViewById(R.id.txtNomApe);
        txtDireccion = (EditText) findViewById(R.id.txtDireccion);
        txtCorreo = (EditText) findViewById(R.id.txtCorreo);
        txtNomUsuario = (EditText) findViewById(R.id.txtNomUsuario);
        txtContra = (EditText) findViewById(R.id.txtContra);
        txtContraConfirm = (EditText) findViewById(R.id.txtContraConfirm);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);
        lblNomApe = (TextView) findViewById(R.id.lblNomApe);
        lblDireccion = (TextView) findViewById(R.id.lblDireccion);
        lblCorreo = (TextView) findViewById(R.id.lblCorreo);

        datos = getIntent().getExtras();
        if(datos!= null && MainActivity.EXISTE_USUARIO == false){
            txtNomUsuario.setText(datos.getString("nomUsu"));
        } else{
            lblNomApe.setVisibility(View.INVISIBLE);
            txtNomApe.setVisibility(View.INVISIBLE);
            lblDireccion.setVisibility(View.INVISIBLE);
            txtDireccion.setVisibility(View.INVISIBLE);
            lblCorreo.setVisibility(View.INVISIBLE);
            txtCorreo.setVisibility(View.INVISIBLE);
            txtNomUsuario.setText(datos.getString("usuarioCambio"));
            btnRegistrar.setText("CAMBIAR PASSWORD");
            btnRegistrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cambioContra(v);
                }
            });
        }

    }

    private void conectar() throws InterruptedException {
        String nomApellidos = txtNomApe.getText().toString();
        String direccion = txtDireccion.getText().toString();
        String mail = txtCorreo.getText().toString();
        String usuario = txtNomUsuario.getText().toString();
        String contrasenia = txtContra.getText().toString();
        HiloInsercionesModificaciones hilo = new HiloInsercionesModificaciones(nomApellidos, direccion, mail, usuario, contrasenia);
        Thread thread = new Thread(hilo);
        thread.start();
        thread.join();
    }

    private void conectar2() throws InterruptedException {
        String usuario = txtNomUsuario.getText().toString();
        String contrasenia = txtContra.getText().toString();
        HiloInsercionesModificaciones hilo = new HiloInsercionesModificaciones(usuario, contrasenia);
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
        String nomApellidos = txtNomApe.getText().toString();
        String direccion = txtDireccion.getText().toString();
        String mail = txtCorreo.getText().toString();
        String usuario = txtNomUsuario.getText().toString();
        String contrasenia = txtContra.getText().toString();
        String contraseniaConfirm = txtContraConfirm.getText().toString();
        if((nomApellidos.isEmpty() == true) && (direccion.isEmpty() == true) && (mail.isEmpty() == true) && (usuario.isEmpty() == true) && (contrasenia.equals(contraseniaConfirm) == false)){
            Toast.makeText(this, R.string.camposVaciosRegistro, Toast.LENGTH_SHORT).show();
        } else if(contrasenia.equals(contraseniaConfirm) == false){
            Toast.makeText(this, R.string.contrasNoCoinciden, Toast.LENGTH_SHORT).show();
        } else if(usuario.isEmpty() == true){
            Toast.makeText(this, R.string.nomUsuarioVacio, Toast.LENGTH_SHORT).show();
        } else if(contrasenia.isEmpty() == true || contraseniaConfirm.isEmpty() == true) {
            Toast.makeText(this, R.string.contrasVacias, Toast.LENGTH_SHORT).show();
        } else if(nomApellidos.isEmpty() == true){
            Toast.makeText(this, R.string.nomUsuarioVacio, Toast.LENGTH_SHORT).show();
        } else if(direccion.isEmpty() == true){
            Toast.makeText(this, R.string.nomUsuarioVacio, Toast.LENGTH_SHORT).show();
        } else if(mail.isEmpty() == true){
            Toast.makeText(this, R.string.nomUsuarioVacio, Toast.LENGTH_SHORT).show();
        } else{
            try {
                if (isConnected()) {
                    conectar();
                } else {
                    Toast.makeText(getApplicationContext(), "ERROR_NO_INTERNET", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {// This cannot happen!
                Toast.makeText(getApplicationContext(), "ERROR_GENERAL", Toast.LENGTH_SHORT).show();
            }
            AlertDialog.Builder msj = new AlertDialog.Builder(this);
            msj.setTitle(R.string.tituloUsuCreado);
            msj.setMessage(R.string.descriUsuCreado);
            msj.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            AlertDialog mostrarMensaje = msj.create();
            mostrarMensaje.show();
        }
    }

    public void cambioContra(View v){
        String usuario = txtNomUsuario.getText().toString();
        String contrasenia = txtContra.getText().toString();
        String contraseniaConfirm = txtContraConfirm.getText().toString();
        if((usuario.isEmpty() == true) && (contrasenia.equals(contraseniaConfirm) == false)){
            Toast.makeText(this, R.string.camposVaciosRegistro, Toast.LENGTH_SHORT).show();
        } else if(contrasenia.equals(contraseniaConfirm) == false){
            Toast.makeText(this, R.string.contrasNoCoinciden, Toast.LENGTH_SHORT).show();
        } else if(usuario.isEmpty() == true){
            Toast.makeText(this, R.string.nomUsuarioVacio, Toast.LENGTH_SHORT).show();
        } else if(contrasenia.isEmpty() == true || contraseniaConfirm.isEmpty() == true) {
            Toast.makeText(this, R.string.contrasVacias, Toast.LENGTH_SHORT).show();
        } else {
            try {
                if (isConnected()) {
                    conectar2();
                } else {
                    Toast.makeText(getApplicationContext(), "ERROR_NO_INTERNET", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {// This cannot happen!
                Toast.makeText(getApplicationContext(), "ERROR_GENERAL", Toast.LENGTH_SHORT).show();
            }
            AlertDialog.Builder msj = new AlertDialog.Builder(this);
            msj.setTitle(R.string.tituloUsuCreado);
            msj.setMessage(R.string.descriUsuCreado);
            msj.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            AlertDialog mostrarMensaje = msj.create();
            mostrarMensaje.show();
        }
    }
}