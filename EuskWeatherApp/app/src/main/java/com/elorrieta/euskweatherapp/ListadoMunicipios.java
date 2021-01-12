package com.elorrieta.euskweatherapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListadoMunicipios extends AppCompatActivity {

    private Button btnBizkaia, btnAraba, btnGipuzkoa;
    private ListView listViewMunicipios;
    private ArrayList<String> listaMunicipios;
    private ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_municipios);

        btnBizkaia = (Button) findViewById(R.id.btnBizkaia);
        btnAraba = (Button) findViewById(R.id.btnAraba);
        btnGipuzkoa = (Button) findViewById(R.id.btnGipuzkoa);
        listViewMunicipios = (ListView) findViewById(R.id.listViewMunicipios);
        registerForContextMenu(listViewMunicipios);

        btnBizkaia.setBackgroundColor(Color.rgb(28, 237, 253));
        btnAraba.setBackgroundColor(Color.rgb(28, 237, 253));
        btnGipuzkoa.setBackgroundColor(Color.rgb(28, 237, 253));

        listViewMunicipios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), InformacionMunicipio.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.menuopciones, m);
        MenuItem itemCambioContra = m.findItem(R.id.itemCambioPass);
        itemCambioContra.setVisible(false);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        menu.setHeaderTitle(R.string.tituloMenuContextual);
        inflater.inflate(R.menu.menulistados, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem menuItem){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuItem.getMenuInfo();
        switch (menuItem.getItemId()){
            case R.id.itemInfoMunicipio:

                break;
            case R.id.itemAgregarImgMuni:

                break;
        }
        return super.onContextItemSelected(menuItem);
    }

    public void mostrarBizkaia(View v){
        btnBizkaia.setBackgroundColor(Color.rgb(140, 105, 178));
        btnAraba.setBackgroundColor(Color.rgb(28, 237, 253));
        btnGipuzkoa.setBackgroundColor(Color.rgb(28, 237, 253));
        String prueba = "Hola mundo";

        listaMunicipios = new ArrayList<>();
        listaMunicipios.add(prueba);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaMunicipios);
        listViewMunicipios.setAdapter(arrayAdapter);
    }

    public void mostrarAraba(View v){
        btnAraba.setBackgroundColor(Color.rgb(140, 105, 178));
        btnBizkaia.setBackgroundColor(Color.rgb(28, 237, 253));
        btnGipuzkoa.setBackgroundColor(Color.rgb(28, 237, 253));
    }

    public void mostrarGipuzkoa(View v){
        btnGipuzkoa.setBackgroundColor(Color.rgb(140, 105, 178));
        btnBizkaia.setBackgroundColor(Color.rgb(28, 237, 253));
        btnAraba.setBackgroundColor(Color.rgb(28, 237, 253));
    }
}