package com.utad.integrador

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import com.squareup.picasso.Picasso

class VistaNuevoPartido : Fragment()  {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vista_nuevo_partido, container, false)
    }
    private lateinit var equipoLocal: String
    private lateinit var equipoVisitante: String
    private lateinit var tvError: TextView
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/b/bd/Logo_Primera_RFEF.png").into(view.findViewById<ImageView>(R.id.logo))

        val spinnerLocal: Spinner = view.findViewById(R.id.spinnerLocal)
        val spinnerVisitante: Spinner = view.findViewById(R.id.spinnerVisitante)
        // Create an ArrayAdapter using the string array and a default spinner layout
        val btnEmpezar = view.findViewById<Button>(R.id.empezar)
        tvError = view.findViewById<TextView>(R.id.tvError)
        this.context?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.equipos_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                // Specify the layout to use when the list of choices appears
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                // Apply the adapter to the spinner
                spinnerLocal.adapter = adapter
                spinnerVisitante.adapter = adapter
            }
        }

        spinnerLocal.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                equipoLocal =  parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

        }

        spinnerVisitante.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                equipoVisitante =  parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
        btnEmpezar.setOnClickListener {
            if(equipoLocal == equipoVisitante){
                tvError.text = "No pueden ser el mismo equipo"
            } else {
                tvError.text = ""
                var fragment = VistaCompletarPartido()
                val bundle = Bundle()
                bundle.putString("local", equipoLocal)
                bundle.putString("visitante", equipoVisitante)
                fragment.arguments = bundle
                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.main_container, fragment)?.addToBackStack("VistaNuevoPartido")?.commit()

            }
        }
    }

}