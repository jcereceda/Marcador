package com.utad.integrador.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.utad.integrador.R
import com.utad.integrador.model.Partido

class PartidosAdapter (
    private var miDataSet: List<Partido>,
    var onClick: (Partido) -> Unit
    ): RecyclerView.Adapter<PartidosAdapter.ViewHolder>() {

    inner class ViewHolder (var view: View):RecyclerView.ViewHolder(view) {
        var local = view.findViewById<TextView>(R.id.local)
        var visitante = view.findViewById<TextView>(R.id.visitante)
        fun bindItems(data: Partido) {
            local.text = data.equipoLocal.nombre
            visitante.text = data.equipoVisitante.nombre
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_lista_partidos, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = miDataSet.get(position)
        holder.bindItems(data)
        val elemento = holder.view.findViewById<CardView>(R.id.card)
        elemento.setOnClickListener{
            onClick(miDataSet[position])
        }
    }

    override fun getItemCount(): Int {
        return miDataSet.size
    }

    fun applyFilter(data: List<Partido>) {
        miDataSet = data
        notifyDataSetChanged()
    }


}