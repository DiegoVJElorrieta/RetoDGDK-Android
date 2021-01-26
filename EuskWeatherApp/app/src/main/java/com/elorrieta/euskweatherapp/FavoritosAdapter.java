package com.elorrieta.euskweatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoritosAdapter extends RecyclerView.Adapter<FavoritosAdapter.MiViewHolder> {

    private List<Favoritos> favoritosList;
    private final OnItemClickListener listener;

    /*** Clase ViewHolder* */
    public class MiViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreMuni;
        public TextView separador;

        public MiViewHolder(View view) {
            super(view);
            nombreMuni = (TextView) view.findViewById(R.id.nomMuni);
            separador = (TextView) view.findViewById(R.id.separador);
        }
    }// Constructor del Adaptador.

    public FavoritosAdapter(List<Favoritos> favoritosList, OnItemClickListener listener) {
        this.favoritosList = favoritosList;
        this.listener = listener;
    }
    // Este metodo es llamado por el RecyclerView para mostrar los datos del elemento de esa posición.

    @Override
    public void onBindViewHolder(FavoritosAdapter.MiViewHolder holder, int position) {
        Favoritos fav = favoritosList.get(position);
        holder.nombreMuni.setText("Nombre municipio: " + fav.getNombreMuni());
        holder.separador.setText("-----------------------------------");
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                listener.onItemClick(fav);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoritosList.size();
    }

    // A este metodo se le llama cuando necesitamos crear una nueva linea para el RecyclerView.
    @Override
    public MiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.linea_favoritos, parent, false);
        return new MiViewHolder(v);
    }
}


