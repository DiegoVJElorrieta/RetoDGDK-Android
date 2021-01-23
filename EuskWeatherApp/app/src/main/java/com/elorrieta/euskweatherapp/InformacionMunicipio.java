package com.elorrieta.euskweatherapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;

public class InformacionMunicipio extends AppCompatActivity {

    private TextView lblNomMunicipio, txtNomMunicipio, txtCoordenadas, txtHumedad, txtTemperatura, txtClima;
    private String rutaImagen;
    RelativeLayout relativeLayout;
    private Snackbar mensajePermisos;
    private static int REQUEST_CODE_ASK_PERMISSION = 111;
    private boolean PERMISOS_FOTOS_ALMACENAMIENTO_CONCEDIDOS, PERMISO_MAPA_CONCEDIDO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_municipio);

        relativeLayout = findViewById(R.id.relativeLayout);
        lblNomMunicipio = (TextView) findViewById(R.id.lblNomMunicipio);
        txtNomMunicipio = (TextView) findViewById(R.id.txtNomMunicipio);
        txtCoordenadas = (TextView) findViewById(R.id.txtCoordenadas);
        txtHumedad = (TextView) findViewById(R.id.txtHumedad);
        txtTemperatura = (TextView) findViewById(R.id.txtTemperatura);
        txtClima = (TextView) findViewById(R.id.txtClima);

        Bundle extras = getIntent().getExtras();

        txtNomMunicipio.setText(extras.getString("nombre"));
        lblNomMunicipio.setText(extras.getString("info"));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.menuopciones, m);
        MenuItem itemCerrarSesion = m.findItem(R.id.itemCerrarSesion);
        MenuItem itemCamara = m.findItem(R.id.camara);
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
        if(id == R.id.camara){
            solicitarPermisos();
            if (PERMISOS_FOTOS_ALMACENAMIENTO_CONCEDIDOS == true) {
                abrirCamara();
            } /*else{
                mensajePermisos.make(relativeLayout, "ES NECESARIO ACEPTAR LOS PERMISOS", Snackbar.LENGTH_LONG).show();
                mensajePermisos.setAction("ENTENDIDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mensajePermisos.dismiss();
                    }
                });
                mensajePermisos.setActionTextColor(Color.RED);
            }*/
        }

        return super.onOptionsItemSelected(mi);
    }

    private void solicitarUbicacion(){
        int permisoMapa = ActivityCompat.checkSelfPermission(InformacionMunicipio.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(permisoMapa != PackageManager.PERMISSION_GRANTED){
            PERMISO_MAPA_CONCEDIDO = false;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSION);
            }
        } else {
            PERMISO_MAPA_CONCEDIDO = true;
        }
    }

    public void btnMapa(View v){
        //solicitarUbicacion();
        //if(PERMISO_MAPA_CONCEDIDO == true){
            Intent i = new Intent(this, MapActivity.class);
            startActivity(i);
        /*} else{
            mensajePermisos.make(relativeLayout, "ES NECESARIO PERMITIR LA UBICACION", Snackbar.LENGTH_LONG).show();
            mensajePermisos.setAction("ENTENDIDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mensajePermisos.dismiss();
                }
            });
            mensajePermisos.setActionTextColor(Color.RED);
        }*/

    }

    private void solicitarPermisos(){
        int permisoStorage = ActivityCompat.checkSelfPermission(InformacionMunicipio.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permisoCamara = ActivityCompat.checkSelfPermission(InformacionMunicipio.this, Manifest.permission.CAMERA);

        if(permisoStorage != PackageManager.PERMISSION_GRANTED || permisoCamara != PackageManager.PERMISSION_GRANTED){
            PERMISOS_FOTOS_ALMACENAMIENTO_CONCEDIDOS = false;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSION);
            }
        } else{
            PERMISOS_FOTOS_ALMACENAMIENTO_CONCEDIDOS = true;
        }
    }

    private void abrirCamara(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //if(i.resolveActivity(getPackageManager()) != null){
            File imagenArchivo = null;

            try{
                imagenArchivo = crearImagen();
            } catch(IOException e){
                Log.e("ERROR", e.toString());
            }

            if(imagenArchivo != null){
                Uri fotoUri = FileProvider.getUriForFile(this, "com.elorrieta.euskweatherapp.fileprovider", imagenArchivo);
                i.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
                startActivityForResult(i, 1);
            }
        //}
    }

    private File crearImagen() throws IOException {
        String nombreImagen = "foto_";
        File directorio = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        File imagen = File.createTempFile(nombreImagen, ".jpg", directorio);

        rutaImagen = imagen.getAbsolutePath();
        return imagen;

    }

}