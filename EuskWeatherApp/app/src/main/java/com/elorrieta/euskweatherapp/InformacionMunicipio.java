package com.elorrieta.euskweatherapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class InformacionMunicipio extends AppCompatActivity {

    private TextView lblNomMunicipio, txtNomMunicipio, txtCoordenadas, txtHumedad, txtTemperatura, txtClima;
    private String rutaImagen;
    private ImageView imagenCamara;
    RelativeLayout relativeLayout;
    private static int REQUEST_CODE_ASK_PERMISSION = 0;
    public static String fotoString;
    public static Date fecha;
    public static double presionAtm;
    public static int temperatura, saturacion;

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
        imagenCamara = (ImageView) findViewById(R.id.imagenCamara);

        Bundle extras = getIntent().getExtras();

        txtNomMunicipio.setText(extras.getString("nombre"));
        lblNomMunicipio.setText(extras.getString("info"));

       /*HiloConsultarInfoMeteo hiloConsultarInfoMeteo = new HiloConsultarInfoMeteo(txtNomMunicipio.getText().toString());
        Thread threadInfoMeteo = new Thread(hiloConsultarInfoMeteo);
        threadInfoMeteo.start();
        try {
            threadInfoMeteo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        fecha = (Date) Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        String strDate = dateFormat.format(fecha);
        txtHumedad.setText(strDate);
        txtCoordenadas.setText(Double.toString(presionAtm));
        txtTemperatura.setText(Integer.toString(temperatura));
        txtClima.setText(Integer.toString(saturacion));*/


        HiloCargarFoto hiloCargarFoto = new HiloCargarFoto(txtNomMunicipio.getText().toString());
        Thread thread = new Thread(hiloCargarFoto);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            byte[] decodedString = Base64.decode(fotoString, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            imagenCamara.setImageBitmap(decodedByte);
        } catch(Exception e){

        }

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
            if(ActivityCompat.checkSelfPermission(InformacionMunicipio.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSION);
            } else{
                abrirCamara();
            }
        }

        return super.onOptionsItemSelected(mi);
    }

    public void btnMapa(View v){
        if(ActivityCompat.checkSelfPermission(InformacionMunicipio.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSION);
        } else{
            Intent i = new Intent(this, MapActivity.class);
            startActivity(i);
        }

    }


    private void abrirCamara(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, 1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imgBitMap = (Bitmap) extras.get("data");
            imagenCamara.setImageBitmap(imgBitMap);
            fotoString = convertirImgString(imgBitMap);
            HiloInsertarFoto hiloInsertarFoto = new HiloInsertarFoto(fotoString, txtNomMunicipio.getText().toString());
            Thread thread = new Thread(hiloInsertarFoto);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private String convertirImgString(Bitmap bitmap){
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, arrayOutputStream);
        byte[] imagenByte = arrayOutputStream.toByteArray();
        String imagenString = Base64.encodeToString(imagenByte, Base64.DEFAULT);

        return imagenString;
    }

    private File crearImagen() throws IOException {
        String nombreImagen = "foto_";
        File directorio = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        File imagen = File.createTempFile(nombreImagen, ".jpg", directorio);

        rutaImagen = imagen.getAbsolutePath();
        return imagen;
    }

}