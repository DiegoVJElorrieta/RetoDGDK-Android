package com.elorrieta.euskweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.Map;

public class InformacionMunicipio extends AppCompatActivity {

    private TextView lblNomMunicipio, txtNomMunicipio, txtCoordenadas, txtHumedad, txtTemperatura, txtClima;

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

        Bundle extras = getIntent().getExtras();

        txtNomMunicipio.setText(extras.getString("nombre"));
        lblNomMunicipio.setText(extras.getString("info"));

    }

    public void btnMapa(View v){
        Intent i = new Intent(this, MapActivity.class);
        startActivity(i);
    }

}