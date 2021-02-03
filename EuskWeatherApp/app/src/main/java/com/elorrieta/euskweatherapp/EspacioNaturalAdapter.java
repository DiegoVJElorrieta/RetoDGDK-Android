package com.elorrieta.euskweatherapp;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EspacioNaturalAdapter extends RecyclerView.Adapter<EspacioNaturalAdapter.MiViewHolder> {

    private List<EspacioNatural> espacioNaturalList;
    private final OnItemClickListener listener;

    /*** Clase ViewHolder* */
    public class MiViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreEspNat;
        public TextView descripcion;
        public TextView tipo;
        public TextView separador;

        public MiViewHolder(View view) {
            super(view);
            nombreEspNat = (TextView) view.findViewById(R.id.nomEspNat);
            descripcion = (TextView) view.findViewById(R.id.descripcionEspNat);
            tipo = (TextView) view.findViewById(R.id.tipoEspNat);
            separador = (TextView) view.findViewById(R.id.separador);
        }
    }// Constructor del Adaptador.

    public EspacioNaturalAdapter(List<EspacioNatural> espacioNaturalList, OnItemClickListener listener) {
        this.espacioNaturalList = espacioNaturalList;
        this.listener = listener;
    }
    // Este metodo es llamado por el RecyclerView para mostrar los datos del elemento de esa posición.

    @Override
    public void onBindViewHolder(EspacioNaturalAdapter.MiViewHolder holder, int position) {
        EspacioNatural en = espacioNaturalList.get(position);
        holder.nombreEspNat.setText(holder.nombreEspNat.getText() + en.getNombreEspacioNat());
        holder.descripcion.setText(holder.descripcion.getText() + en.getDescripcion());
        holder.tipo.setText(holder.tipo.getText() + en.getTipo());
        holder.separador.setText("-----------------------------------");
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                listener.onItemClick(en);
            }
        });
    }

    @Override
    public int getItemCount() {
        return espacioNaturalList.size();
    }

    // A este metodo se le llama cuando necesitamos crear una nueva linea para el RecyclerView.
    @Override
    public EspacioNaturalAdapter.MiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.linea_espacio_natural, parent, false);
        return new MiViewHolder(v);
    }
}