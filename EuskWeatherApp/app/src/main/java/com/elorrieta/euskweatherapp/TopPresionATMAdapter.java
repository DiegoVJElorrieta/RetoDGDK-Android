package com.elorrieta.euskweatherapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TopPresionATMAdapter extends RecyclerView.Adapter<TopPresionATMAdapter.MiViewHolder> {

    private List<TopPresionATM> presionATMList;

    /*** Clase ViewHolder* */
    public class MiViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreProv;
        public TextView nombreMuni;
        public TextView Presion;
        public TextView separador;

        public MiViewHolder(View view) {
            super(view);
            nombreProv = (TextView) view.findViewById(R.id.nomProv);
            nombreMuni = (TextView) view.findViewById(R.id.nombMuni);
            Presion = (TextView) view.findViewById(R.id.presion);
            separador = (TextView) view.findViewById(R.id.separador);
        }
    }// Constructor del Adaptador.

    public TopPresionATMAdapter(List<TopPresionATM> presionATMList) {
        this.presionATMList = presionATMList;
    }
    // Este metodo es llamado por el RecyclerView para mostrar los datos del elemento de esa posición.

    @Override
    public void onBindViewHolder(MiViewHolder holder, int position) {
        TopPresionATM topATM = presionATMList.get(position);
        holder.nombreProv.setText("Nombre Provincia: " + topATM.getNomProv());
        holder.nombreMuni.setText("Municipio: " + topATM.getNomMuni());
        holder.Presion.setText("Presion Atmosferica: " + topATM.getPresion());
        holder.separador.setText("-----------------------------------");
    }

    @Override
    public int getItemCount() {
        return presionATMList.size();
    }

    // A este metodo se le llama cuando necesitamos crear una nueva linea para el RecyclerView.
    @Override
    public MiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.linea_presion, parent, false);
        return new MiViewHolder(v);
    }
}
