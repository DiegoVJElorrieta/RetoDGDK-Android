package com.elorrieta.euskweatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class ListadoMunicipios extends AppCompatActivity {

    private Button btnBizkaia, btnAraba, btnGipuzkoa;
    private ListView listViewMunicipios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_municipios);

        btnBizkaia = (Button) findViewById(R.id.btnBizkaia);
        btnAraba = (Button) findViewById(R.id.btnAraba);
        btnGipuzkoa = (Button) findViewById(R.id.btnGipuzkoa);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        menu.setHeaderTitle("Pulsa sobre la opcion deseada");
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
}