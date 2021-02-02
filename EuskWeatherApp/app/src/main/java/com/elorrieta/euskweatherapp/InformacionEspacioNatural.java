package com.elorrieta.euskweatherapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import java.util.ArrayList;

public class InformacionEspacioNatural extends AppCompatActivity {

    private TextView lblNomEspNat, lblTipo, txtTemperaturaEsp, txtCalidadAire;
    private ImageView imagenCamara;
    private static int REQUEST_CODE_ASK_PERMISSION = 0;
    private  String fotoString;
    private ArrayList<InformacionMeteorologica> listadoInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_espacio_natural);

        lblNomEspNat = (TextView) findViewById(R.id.txtNomEspNat);
        lblTipo = (TextView) findViewById(R.id.lblTipoEspNat);
        imagenCamara = (ImageView) findViewById(R.id.imgEspNat);
        txtTemperaturaEsp = (TextView) findViewById(R.id.txtTempEsp);
        txtCalidadAire = (TextView) findViewById(R.id.txtCalidadAire);

        Bundle extras = getIntent().getExtras();

        lblNomEspNat.setText(extras.getString("nombreEspNat"));
        lblTipo.setText(extras.getString("tipoEspNat"));

        listadoInfo = new ArrayList<>();
        HiloInfoMeteoEspNat hiloInfoMeteoEspNat = new HiloInfoMeteoEspNat(listadoInfo, lblNomEspNat.getText().toString());
        Thread thread = new Thread(hiloInfoMeteoEspNat);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(listadoInfo.isEmpty()){
            txtTemperaturaEsp.setText("INFO NO DISPONIBLE");
            txtCalidadAire.setText("INFO NO DISPONIBLE");
        } else{
            txtTemperaturaEsp.setText(listadoInfo.get(0).getTemperatura() + "ºC");
            txtCalidadAire.setText(listadoInfo.get(0).getCalidadAire());
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.menuopciones, m);
        MenuItem itemCerrarSesion = m.findItem(R.id.itemCerrarSesion);
        MenuItem itemCamara = m.findItem(R.id.camara);
        MenuItem itemCompartir = m.findItem(R.id.compartir);
        itemCompartir.setVisible(false);
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
        if(id == R.id.camara){
            if(ActivityCompat.checkSelfPermission(InformacionEspacioNatural.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "HOLA", Toast.LENGTH_SHORT).show();
                //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSION);
            } else{
                abrirCamara();
            }
        }

        return super.onOptionsItemSelected(mi);
    }

    public void btnMapaEspNat(View v){
        if(ActivityCompat.checkSelfPermission(InformacionEspacioNatural.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
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
        }
    }

    private String convertirImgString(Bitmap bitmap){
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, arrayOutputStream);
        byte[] imagenByte = arrayOutputStream.toByteArray();
        String imagenString = Base64.encodeToString(imagenByte, Base64.DEFAULT);
        return imagenString;
    }
}