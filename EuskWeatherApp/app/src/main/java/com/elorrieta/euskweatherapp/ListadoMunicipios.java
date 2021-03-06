package com.elorrieta.euskweatherapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;
import java.util.ArrayList;

public class ListadoMunicipios extends AppCompatActivity {

    public static boolean EXISTE_FAVORITO;
    private Button btnBizkaia, btnAraba, btnGipuzkoa;
    private RecyclerView recyclerViewMunicipios;
    private ArrayList<Municipio> listaMunicipios;
    private ArrayAdapter<String> arrayAdapter;
    private ConnectivityManager connectivityManager = null;
    public static boolean CONSULTA_MUNICIPIOS;
    public static int idProvincia;
    public static String nomMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado_municipios);

        btnBizkaia = (Button) findViewById(R.id.btnBizkaia);
        btnAraba = (Button) findViewById(R.id.btnAraba);
        btnGipuzkoa = (Button) findViewById(R.id.btnGipuzkoa);
        recyclerViewMunicipios = (RecyclerView) findViewById(R.id.recyclerViewMunicipios);

        btnBizkaia.setBackgroundColor(Color.rgb(28, 237, 253));
        btnAraba.setBackgroundColor(Color.rgb(28, 237, 253));
        btnGipuzkoa.setBackgroundColor(Color.rgb(28, 237, 253));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m){
        getMenuInflater().inflate(R.menu.menuopciones, m);
        MenuItem itemCerrarSesion = m.findItem(R.id.itemCerrarSesion);
        MenuItem itemCamara = m.findItem(R.id.camara);
        itemCamara.setVisible(false);
        itemCerrarSesion.setVisible(false);
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

    private ArrayList conectar() throws InterruptedException {
        ClientThread clientThread = new ClientThread(listaMunicipios);
        Thread thread = new Thread(clientThread);
        thread.start();
        thread.join(); // Esperar respusta del servidor...
        return clientThread.getResponse();
    }

    public boolean isConnected() {
        boolean ret = false;
        try {
            connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if ((networkInfo != null) && (networkInfo.isAvailable()) && (networkInfo.isConnected()))
                ret = true;
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error_comunicación", Toast.LENGTH_SHORT).show();
        }
        return ret;
    }

    public void mostrarBizkaia(View v){
        idProvincia = 48;
        btnBizkaia.setBackgroundColor(Color.rgb(140, 105, 178));
        btnAraba.setBackgroundColor(Color.rgb(28, 237, 253));
        btnGipuzkoa.setBackgroundColor(Color.rgb(28, 237, 253));
        String sRespuesta = null;
        listaMunicipios = new ArrayList<>();
        try {
            if (isConnected()) {
                CONSULTA_MUNICIPIOS = true;
                listaMunicipios.clear();
                listaMunicipios = conectar();
            } else {
                Toast.makeText(getApplicationContext(), "ERROR_NO_INTERNET", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException e) {// This cannot happen!
            Toast.makeText(getApplicationContext(), "ERROR_GENERAL", Toast.LENGTH_SHORT).show();
        }

        MunicipioAdapter ma = new MunicipioAdapter(listaMunicipios, new OnItemClickListener() {
            @Override
            public void onItemClick(Municipio item) {
                EXISTE_FAVORITO = false;
                AlertDialog.Builder mensaje = new AlertDialog.Builder(ListadoMunicipios.this);
                mensaje.setTitle("INFO DE MUNICIPIO");
                mensaje.setMessage("Nombre: " + item.getNombreMuni() + "\n" +
                        "Alcalde: " + item.getAlcaldeMuni() + "\n" +
                        "Pagina web: " + item.getWebMuni());
                nomMapa = item.getNombreMuni();
                HiloComprobarFavoritos comprobarFavoritos = new HiloComprobarFavoritos(item.getNombreMuni());
                Thread thread = new Thread(comprobarFavoritos);
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(EXISTE_FAVORITO == false) {
                    mensaje.setNeutralButton("AÑADIR A FAVORITOS", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            HiloInsertarFavoritos insertarFavoritos = new HiloInsertarFavoritos(item.getNombreMuni());
                            Thread thread2 = new Thread(insertarFavoritos);
                            thread2.start();
                            try {
                                thread2.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else {
                    mensaje.setNeutralButton("ELIMINAR DE FAVORITOS", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            HiloEliminarFavoritos borrarFavoritos = new HiloEliminarFavoritos(item.getNombreMuni());
                            Thread thread3 = new Thread(borrarFavoritos);
                            thread3.start();
                            try {
                                thread3.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                mensaje.setNegativeButton("CERRAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                mensaje.setPositiveButton("MAS INFORMACION", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(), InformacionMunicipio.class);
                        i.putExtra("nombre",item.getNombreMuni());
                        i.putExtra("info","Alcalde: " + item.getAlcaldeMuni() + "\n" +
                                "Pagina web: " + item.getWebMuni());
                        startActivity(i);
                    }
                });
                mensaje.show();
            }

            @Override
            public void onItemClick(EspacioNatural item) {

            }

            @Override
            public void onItemClick(Favoritos item) {

            }
        });
        recyclerViewMunicipios.setAdapter(ma);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewMunicipios.setLayoutManager(linearLayoutManager);
        CONSULTA_MUNICIPIOS = false;
    }

    public void mostrarAraba(View v){
        idProvincia = 1;
        btnAraba.setBackgroundColor(Color.rgb(140, 105, 178));
        btnBizkaia.setBackgroundColor(Color.rgb(28, 237, 253));
        btnGipuzkoa.setBackgroundColor(Color.rgb(28, 237, 253));

        String sRespuesta = null;
        listaMunicipios = new ArrayList<>();
        try {
            if (isConnected()) {
                CONSULTA_MUNICIPIOS = true;
                listaMunicipios.clear();
                listaMunicipios = conectar();
            } else {
                Toast.makeText(getApplicationContext(), "ERROR_NO_INTERNET", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException e) {// This cannot happen!
            Toast.makeText(getApplicationContext(), "ERROR_GENERAL", Toast.LENGTH_SHORT).show();
        }

        MunicipioAdapter ma = new MunicipioAdapter(listaMunicipios, new OnItemClickListener() {
            @Override
            public void onItemClick(Municipio item) {
                EXISTE_FAVORITO = false;
                AlertDialog.Builder mensaje = new AlertDialog.Builder(ListadoMunicipios.this);
                mensaje.setTitle("INFO DE MUNICIPIO");
                mensaje.setMessage("Nombre: " + item.getNombreMuni() + "\n" +
                        "Alcalde: " + item.getAlcaldeMuni() + "\n" +
                        "Pagina web: " + item.getWebMuni());
                nomMapa = item.getNombreMuni();
                HiloComprobarFavoritos comprobarFavoritos = new HiloComprobarFavoritos(item.getNombreMuni());
                Thread thread = new Thread(comprobarFavoritos);
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(EXISTE_FAVORITO == false) {
                    mensaje.setNeutralButton("AÑADIR A FAVORITOS", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            HiloInsertarFavoritos insertarFavoritos = new HiloInsertarFavoritos(item.getNombreMuni());
                            Thread thread2 = new Thread(insertarFavoritos);
                            thread2.start();
                            try {
                                thread2.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else {
                    mensaje.setNeutralButton("ELIMINAR DE FAVORITOS", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            HiloEliminarFavoritos borrarFavoritos = new HiloEliminarFavoritos(item.getNombreMuni());
                            Thread thread3 = new Thread(borrarFavoritos);
                            thread3.start();
                            try {
                                thread3.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                mensaje.setNegativeButton("CERRAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                mensaje.setPositiveButton("MAS INFORMACION", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(), InformacionMunicipio.class);
                        i.putExtra("nombre",item.getNombreMuni());
                        i.putExtra("info","Alcalde: " + item.getAlcaldeMuni() + "\n" +
                                "Pagina web: " + item.getWebMuni());
                        startActivity(i);
                    }
                });
                mensaje.show();
            }

            @Override
            public void onItemClick(EspacioNatural item) {

            }

            @Override
            public void onItemClick(Favoritos item) {

            }
        });
        recyclerViewMunicipios.setAdapter(ma);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewMunicipios.setLayoutManager(linearLayoutManager);
        CONSULTA_MUNICIPIOS = false;
    }

    public void mostrarGipuzkoa(View v){
        idProvincia = 20;
        btnGipuzkoa.setBackgroundColor(Color.rgb(140, 105, 178));
        btnBizkaia.setBackgroundColor(Color.rgb(28, 237, 253));
        btnAraba.setBackgroundColor(Color.rgb(28, 237, 253));

        String sRespuesta = null;
        listaMunicipios = new ArrayList<>();
        try {
            if (isConnected()) {
                CONSULTA_MUNICIPIOS = true;
                listaMunicipios.clear();
                listaMunicipios = conectar();
            } else {
                Toast.makeText(getApplicationContext(), "ERROR_NO_INTERNET", Toast.LENGTH_SHORT).show();
            }
        } catch (InterruptedException e) {// This cannot happen!
            Toast.makeText(getApplicationContext(), "ERROR_GENERAL", Toast.LENGTH_SHORT).show();
        }

        MunicipioAdapter ma = new MunicipioAdapter(listaMunicipios, new OnItemClickListener() {
            @Override
            public void onItemClick(Municipio item) {
                EXISTE_FAVORITO = false;
                AlertDialog.Builder mensaje = new AlertDialog.Builder(ListadoMunicipios.this);
                mensaje.setTitle("INFO DE MUNICIPIO");
                mensaje.setMessage("Nombre: " + item.getNombreMuni() + "\n" +
                        "Alcalde: " + item.getAlcaldeMuni() + "\n" +
                        "Pagina web: " + item.getWebMuni());
                nomMapa = item.getNombreMuni();
                HiloComprobarFavoritos comprobarFavoritos = new HiloComprobarFavoritos(item.getNombreMuni());
                Thread thread = new Thread(comprobarFavoritos);
                thread.start();
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(EXISTE_FAVORITO == false) {
                    mensaje.setNeutralButton("AÑADIR A FAVORITOS", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            HiloInsertarFavoritos insertarFavoritos = new HiloInsertarFavoritos(item.getNombreMuni());
                            Thread thread2 = new Thread(insertarFavoritos);
                            thread2.start();
                            try {
                                thread2.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }else {
                    mensaje.setNeutralButton("ELIMINAR DE FAVORITOS", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            HiloEliminarFavoritos borrarFavoritos = new HiloEliminarFavoritos(item.getNombreMuni());
                            Thread thread3 = new Thread(borrarFavoritos);
                            thread3.start();
                            try {
                                thread3.join();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }

                mensaje.setNegativeButton("CERRAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                mensaje.setPositiveButton("MAS INFORMACION", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getApplicationContext(), InformacionMunicipio.class);
                        i.putExtra("nombre",item.getNombreMuni());
                        i.putExtra("info","Alcalde: " + item.getAlcaldeMuni() + "\n" +
                                "Pagina web: " + item.getWebMuni());
                        startActivity(i);
                    }
                });
                mensaje.show();
            }

            @Override
            public void onItemClick(EspacioNatural item) {

            }

            @Override
            public void onItemClick(Favoritos item) {

            }
        });
        recyclerViewMunicipios.setAdapter(ma);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewMunicipios.setLayoutManager(linearLayoutManager);
        CONSULTA_MUNICIPIOS = false;
    }
}