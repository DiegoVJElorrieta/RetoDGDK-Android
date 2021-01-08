package com.elorrieta.euskweatherapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistroUsuario extends AppCompatActivity {

    private EditText txtNomApe, txtDireccion, txtCorreo, txtNomUsuario, txtContra, txtContraConfirm;
    private Button btnRegistrar;
    Bundle datos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        txtNomApe = (EditText) findViewById(R.id.txtNomApe);
        txtDireccion = (EditText) findViewById(R.id.txtNomApe);
        txtCorreo = (EditText) findViewById(R.id.txtCorreo);
        txtNomUsuario = (EditText) findViewById(R.id.txtNomUsuario);
        txtContra = (EditText) findViewById(R.id.txtContra);
        txtContraConfirm = (EditText) findViewById(R.id.txtContraConfirm);
        btnRegistrar = (Button) findViewById(R.id.btnRegistrar);

        datos = getIntent().getExtras();
        if(datos!= null && MainActivity.EXISTE_USUARIO == false){
            txtNomUsuario.setText(datos.getString("nomUsu"));
        }

    }

    public void registrarUsuario(View v){
        String usuario = txtNomUsuario.getText().toString();
        String contrasenia = txtContra.getText().toString();
        String contraseniaConfirm = txtContraConfirm.getText().toString();
        SharedPreferences users = getSharedPreferences("usuarios", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = users.edit();
        if((usuario.isEmpty() == true) && (contrasenia.equals(contraseniaConfirm) == false)){
            Toast.makeText(this, "NO PUEDES DEJAR NADA VACIO", Toast.LENGTH_SHORT).show();
        } else if(contrasenia.equals(contraseniaConfirm) == false){
            Toast.makeText(this, "NO COINCIDEN", Toast.LENGTH_SHORT).show();
        } else if(usuario.isEmpty() == true){
            Toast.makeText(this, "DEBES ESTABLECER NOMBRE USUARIO", Toast.LENGTH_SHORT).show();
        } else if(contrasenia.isEmpty() == true || contraseniaConfirm.isEmpty() == true) {
            Toast.makeText(this, "RELLENA LAS CONTRASEÑAS", Toast.LENGTH_SHORT).show();
        } else{
            editor.putString(usuario, contrasenia);
            editor.commit();
            AlertDialog.Builder msj = new AlertDialog.Builder(this);
            msj.setTitle("USUARIO CREADO");
            msj.setMessage("Pulse ok para regresar a inicio");
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