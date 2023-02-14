package com.utad.integrador.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.utad.integrador.R
import com.utad.integrador.model.Equipo
import com.utad.integrador.model.Marcador

class PartidosAdapter (
    private var miDataSet: List<Marcador>,
    private var listEquipos: List<Equipo>,
    var onClick: (Marcador) -> Unit
    ): RecyclerView.Adapter<PartidosAdapter.ViewHolder>() {

    inner class ViewHolder (var view: View):RecyclerView.ViewHolder(view) {
        var tvLocal = view.findViewById<TextView>(R.id.local)
        var tvVisitante = view.findViewById<TextView>(R.id.visitante)
        fun bindItems(local: Equipo, visitante: Equipo) {
            tvLocal.text = local.nombre
            tvVisitante.text = visitante.nombre
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lista_partidos, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i("punto llegada","punto llegada")
        val data = miDataSet.get(position)

        // Aqui no llega

        // Sacar id de local y visitante y pasar su correspondiente equipo
        val idLocal = data.equipoLocal
        val idVisitante = data.equipoVisitante
        var equipoLocal: Equipo = listEquipos.filter { it.id == idLocal }.single()
        var equipoVisitante: Equipo = listEquipos.filter { it.id == idVisitante }.single()

        Log.e("local",equipoLocal.nombre)
        holder.bindItems(equipoLocal, equipoVisitante)

        val elemento = holder.view.findViewById<CardView>(R.id.card)

        elemento.setOnClickListener{
            onClick(miDataSet[position])
        }
    }

    override fun getItemCount(): Int {
        return miDataSet.size
    }

    fun applyFilter(data: List<Marcador>) {
        miDataSet = data
        notifyDataSetChanged()
    }


}