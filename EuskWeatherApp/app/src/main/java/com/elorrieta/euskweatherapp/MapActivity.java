package com.elorrieta.euskweatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.provider.Telephony;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.LinkedTransferQueue;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap mapa = googleMap;
        Geocoder geocoder = new Geocoder(this);
        int maxResultados = 1;
        try {
            List<Address> address = geocoder.getFromLocationName(ListadoMunicipios.nomMapa, maxResultados);
            LatLng oElorrieta = new LatLng(address.get(0).getLatitude(), address.get(0).getLongitude());
            mapa.addMarker(new MarkerOptions().position(oElorrieta).title("Marker Elorrieta"));
            mapa.moveCamera(CameraUpdateFactory.newLatLng(oElorrieta));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}