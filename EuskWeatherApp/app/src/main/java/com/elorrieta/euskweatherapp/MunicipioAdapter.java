package com.elorrieta.euskweatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MunicipioAdapter extends RecyclerView.Adapter<MunicipioAdapter.MiViewHolder> {

    private List<Municipio> municipioList;
    private final OnItemClickListener listener;

    /*** Clase ViewHolder* */
    public class MiViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreMuni;
        public TextView alcaldeMuni;
        public TextView webMuni;
        public TextView separador;

        public MiViewHolder(View view) {
            super(view);
            nombreMuni = (TextView) view.findViewById(R.id.nomMuni);
            alcaldeMuni = (TextView) view.findViewById(R.id.alcaldeMuni);
            webMuni = (TextView) view.findViewById(R.id.webMuni);
            separador = (TextView) view.findViewById(R.id.separador);
        }
    }// Constructor del Adaptador.

    public MunicipioAdapter(List<Municipio> municipioList, OnItemClickListener listener) {
        this.municipioList = municipioList;
        this.listener = listener;
    }
    // Este metodo es llamado por el RecyclerView para mostrar los datos del elemento de esa posición.

    @Override
    public void onBindViewHolder(MiViewHolder holder, int position) {
        Municipio m = municipioList.get(position);
        holder.nombreMuni.setText("Nombre municipio: " + m.getNombreMuni());
        holder.alcaldeMuni.setText("Alcalde: " + m.getAlcaldeMuni());
        holder.webMuni.setText("Web municipio: " + m.getWebMuni());
        holder.separador.setText("-----------------------------------");
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                listener.onItemClick(m);
            }
        });
    }

    @Override
    public int getItemCount() {
        return municipioList.size();
    }

    // A este metodo se le llama cuando necesitamos crear una nueva linea para el RecyclerView.
    @Override
    public MiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.linea_municipios, parent, false);
        return new MiViewHolder(v);
    }
}
