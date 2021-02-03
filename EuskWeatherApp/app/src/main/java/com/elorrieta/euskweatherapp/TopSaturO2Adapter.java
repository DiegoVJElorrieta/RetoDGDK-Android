package com.elorrieta.euskweatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TopSaturO2Adapter extends RecyclerView.Adapter<TopSaturO2Adapter.MiViewHolder> {

    private List<TopSaturO2> saturO2List;

    /*** Clase ViewHolder* */
    public class MiViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreProv;
        public TextView nombreMuni;
        public TextView satur;
        public TextView separador;

        public MiViewHolder(View view) {
            super(view);
            nombreProv = (TextView) view.findViewById(R.id.nomProv);
            nombreMuni = (TextView) view.findViewById(R.id.nombMuni);
            satur = (TextView) view.findViewById(R.id.satur);
            separador = (TextView) view.findViewById(R.id.separador);
        }
    }// Constructor del Adaptador.

    public TopSaturO2Adapter(List<TopSaturO2> saturO2List) {
        this.saturO2List = saturO2List;
    }
    // Este metodo es llamado por el RecyclerView para mostrar los datos del elemento de esa posición.

    @Override
    public void onBindViewHolder(TopSaturO2Adapter.MiViewHolder holder, int position) {
        TopSaturO2 top = saturO2List.get(position);
        holder.nombreProv.setText(holder.nombreProv.getText() + top.getNomProv());
        holder.nombreMuni.setText(holder.nombreMuni.getText() + top.getNomMuni());
        holder.satur.setText(holder.satur.getText() + top.getSaturO2());
        holder.separador.setText("-----------------------------------");
    }

    @Override
    public int getItemCount() {
        return saturO2List.size();
    }

    // A este metodo se le llama cuando necesitamos crear una nueva linea para el RecyclerView.
    @Override
    public MiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.linea_satur, parent, false);
        return new MiViewHolder(v);
    }
}
