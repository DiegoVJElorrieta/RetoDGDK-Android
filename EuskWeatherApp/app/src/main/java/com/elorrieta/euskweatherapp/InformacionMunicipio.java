package com.elorrieta.euskweatherapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
    public static String calidadAire;
    public static String presionAtm;
    public static String temperatura;
    public static String fecha;
    public static int saturacion;
    private RecyclerView recyclerViewFoto;
    private ArrayList<Foto> fotos;
    private ArrayList<InformacionMeteorologica> listaInfoMeteo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_municipio);

        lblNomMunicipio = (TextView) findViewById(R.id.lblNomMunicipio);
        txtNomMunicipio = (TextView) findViewById(R.id.txtNomMunicipio);
        txtCoordenadas = (TextView) findViewById(R.id.txtCoordenadas);
        txtHumedad = (TextView) findViewById(R.id.txtHumedad);
        txtTemperatura = (TextView) findViewById(R.id.txtTemperatura);
        txtClima = (TextView) findViewById(R.id.txtClima);
        imagenCamara = (ImageView) findViewById(R.id.imagenCamara);
        recyclerViewFoto = (RecyclerView) findViewById(R.id.recyclerViewFotos);
        imagenCamara.setImageResource(R.drawable.logo);
        Bundle extras = getIntent().getExtras();

        txtNomMunicipio.setText(extras.getString("nombre"));
        lblNomMunicipio.setText(extras.getString("info"));

        listaInfoMeteo = new ArrayList<>();
        HiloInformacionMeteo hiloInformacionMeteo = new HiloInformacionMeteo(listaInfoMeteo, txtNomMunicipio.getText().toString());
        Thread hiloInfo = new Thread(hiloInformacionMeteo);
        hiloInfo.start();
        try {
            hiloInfo.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if(listaInfoMeteo.isEmpty()){
            txtCoordenadas.setText(R.string.infoNoDisponible);
            txtHumedad.setText(R.string.infoNoDisponible);
            txtTemperatura.setText(R.string.infoNoDisponible);
            txtClima.setText(R.string.infoNoDisponible);
        }else {
            txtCoordenadas.setText(listaInfoMeteo.get(0).getPresionAtm() + " atm");
            txtHumedad.setText(listaInfoMeteo.get(0).getFecha());
            txtTemperatura.setText(listaInfoMeteo.get(0).getTemperatura() + "ºC");
            txtClima.setText(listaInfoMeteo.get(0).getSaturacionO2() + "%");
        }

        fotos = new ArrayList<>();
        HiloCargarFoto hiloCargarFoto = new HiloCargarFoto(fotos, txtNomMunicipio.getText().toString());
        Thread thread = new Thread(hiloCargarFoto);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        FotoAdapter fa = new FotoAdapter(fotos, new OnItemClickListener() {
            @Override
            public void onItemClick(Municipio item) {

            }

            @Override
            public void onItemClick(EspacioNatural item) {

            }

            @Override
            public void onItemClick(Favoritos item) {

            }

            @Override
            public void onItemClick(Foto item) {
                String codFoto = item.getFotoString();
                byte[] decodedString = Base64.decode(codFoto, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                imagenCamara.setImageBitmap(decodedByte);
            }
        });
        recyclerViewFoto.setAdapter(fa);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewFoto.setLayoutManager(linearLayoutManager);

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
        if(id == R.id.compartir){
            try {
                BitmapDrawable drawable = (BitmapDrawable) imagenCamara.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                compartirFoto(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
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
            //imagenCamara.setImageBitmap(imgBitMap);
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

    private void compartir() throws IOException {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        String aux = "Mira la foto que encontre de " + txtNomMunicipio.getText().toString() + ":\n";
        i.putExtra(Intent.EXTRA_TEXT, aux);

        startActivity(i);
    }

    private void compartirFoto(Bitmap bitmap) throws IOException {
        String aux = R.string.msjCompartirFoto + txtNomMunicipio.getText().toString();
        File cachePath = new File(this.getCacheDir(), "imageview");
        cachePath.mkdirs();
        FileOutputStream stream = new FileOutputStream(cachePath + "/image.png");
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        stream.close();

        File imagePath = new File(this.getCacheDir(), "imageview");
        File newFile = new File(imagePath, "image.png");
        Uri contentUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", newFile);
        if(contentUri != null){
            Intent shareIntent = new Intent();
            shareIntent.putExtra(Intent.EXTRA_TEXT, aux);
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            shareIntent.setType("image/png");
            startActivity(Intent.createChooser(shareIntent, "Choose an app"));
        }
    }

}