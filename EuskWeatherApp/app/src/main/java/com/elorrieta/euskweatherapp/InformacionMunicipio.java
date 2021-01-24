package com.elorrieta.euskweatherapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
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

public class InformacionMunicipio extends AppCompatActivity {

    private TextView lblNomMunicipio, txtNomMunicipio, txtCoordenadas, txtHumedad, txtTemperatura, txtClima;
    private String rutaImagen;
    private ImageView imagenCamara;
    RelativeLayout relativeLayout;
    private static int REQUEST_CODE_ASK_PERMISSION = 0;
    private static String fotoString;

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
                Toast.makeText(this, "HOLA", Toast.LENGTH_SHORT).show();
                //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSION);
            } else{
                abrirCamara();
            }
           /*Snackbar mensajeCamara = Snackbar.make(relativeLayout, "ES NECESARIO ACEPTAR LOS PERMISOS", Snackbar.LENGTH_LONG);
                mensajeCamara.setAction("ENTENDIDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        solicitarPermisos();
                    }
                });
                mensajeCamara.setActionTextColor(Color.RED);
                mensajeCamara.show();*/

        }

        return super.onOptionsItemSelected(mi);
    }

    private void solicitarUbicacion(){
        int permisoMapa = ActivityCompat.checkSelfPermission(InformacionMunicipio.this, Manifest.permission.ACCESS_FINE_LOCATION);
        if(permisoMapa != PackageManager.PERMISSION_GRANTED){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_ASK_PERMISSION);
            }
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
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_CODE_ASK_PERMISSION);
            }
        }
    }

    private void abrirCamara(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
       // File imagenArchivo = null;
        //try{
         //   imagenArchivo = crearImagen();
        //} catch(IOException e){
        //    Log.e("ERROR", e.toString());
        //}
        //if(imagenArchivo != null){
           // Uri fotoUri = FileProvider.getUriForFile(this, "com.elorrieta.euskweatherapp.fileprovider", imagenArchivo);
            //i.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
            startActivityForResult(i, 1);
       // }
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

    private File crearImagen() throws IOException {
        String nombreImagen = "foto_";
        File directorio = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        File imagen = File.createTempFile(nombreImagen, ".jpg", directorio);

        rutaImagen = imagen.getAbsolutePath();
        return imagen;
    }

}