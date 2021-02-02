package com.elorrieta.euskweatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TopTemperaturaAdapter extends RecyclerView.Adapter<TopTemperaturaAdapter.MiViewHolder> {

    private List<TopTemperatura> tempList;

    /*** Clase ViewHolder* */
    public class MiViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreProv;
        public TextView nombreMuni;
        public TextView temperatura;
        public TextView separador;

        public MiViewHolder(View view) {
            super(view);
            nombreProv = (TextView) view.findViewById(R.id.nomProv);
            nombreMuni = (TextView) view.findViewById(R.id.nombMuni);
            temperatura = (TextView) view.findViewById(R.id.temperatura);
            separador = (TextView) view.findViewById(R.id.separador);
        }
    }// Constructor del Adaptador.

    public TopTemperaturaAdapter(List<TopTemperatura> tempList) {
        this.tempList = tempList;
    }
    // Este metodo es llamado por el RecyclerView para mostrar los datos del elemento de esa posición.

    @Override
    public void onBindViewHolder(MiViewHolder holder, int position) {
        TopTemperatura topTemp = tempList.get(position);
        holder.nombreProv.setText("Nombre Provincia: " + topTemp.getNomProv());
        holder.nombreMuni.setText("Municipio: " + topTemp.getNomMuni());
        holder.temperatura.setText("Temperatura: " + topTemp.getTemperatura());
        holder.separador.setText("-----------------------------------");
    }

    @Override
    public int getItemCount() {
        return tempList.size();
    }

    // A este metodo se le llama cuando necesitamos crear una nueva linea para el RecyclerView.
    @Override
    public MiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.linea_temperatura, parent, false);
        return new MiViewHolder(v);
    }
}
