package com.elorrieta.euskweatherapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FotoAdapter extends RecyclerView.Adapter<FotoAdapter.MiViewHolder>{

    private List<Foto> fotos;
    private final OnItemClickListener listener;

    /*** Clase ViewHolder* */
    public class MiViewHolder extends RecyclerView.ViewHolder {
        public ImageView foto;
        public TextView separadorFoto;

        public MiViewHolder(View view) {
            super(view);
            foto = (ImageView) view.findViewById(R.id.imagenRecycler);
            separadorFoto = (TextView) view.findViewById(R.id.separadorFotos);
        }
    }// Constructor del Adaptador.

    public FotoAdapter(List<Foto> fotos, OnItemClickListener listener) {
        this.fotos = fotos;
        this.listener = listener;
    }
    // Este metodo es llamado por el RecyclerView para mostrar los datos del elemento de esa posición.

    @Override
    public void onBindViewHolder(FotoAdapter.MiViewHolder holder, int position) {
        Foto f = fotos.get(position);
        byte[] decodedString = Base64.decode(f.getFotoString(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.foto.setImageBitmap(decodedByte);
        holder.separadorFoto.setText("-------------");
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                listener.onItemClick(f);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fotos.size();
    }

    // A este metodo se le llama cuando necesitamos crear una nueva linea para el RecyclerView.
    @Override
    public FotoAdapter.MiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.linea_fotos, parent, false);
        return new FotoAdapter.MiViewHolder(v);
    }

}
