package com.utad.integrador

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.utad.integrador.Adapter.PartidosAdapter
import com.utad.integrador.databinding.FragmentVistaListadoPartidosBinding
import com.utad.integrador.model.Partido
import com.utad.integrador.model.PartidosRespose
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class VistaListadoPartidos : Fragment() {

    private var _binding: FragmentVistaListadoPartidosBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVistaListadoPartidosBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    private lateinit var todos: PartidosRespose
    private lateinit var partidosFiltrados: ArrayList<Partido>
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val json = readJsonFromFile("partidos.json")
        todos = Gson().fromJson(json, PartidosRespose::class.java)

        val myAdapter = PartidosAdapter(todos.data) {

            val fragment = VistaDatosPartido()
            val bundle = Bundle()
            bundle.putSerializable("miPartido", it)
            fragment.arguments = bundle
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.main_container, fragment)?.addToBackStack("VistaListadoPartidos")
                ?.commit()
        }
        var partidosRecyclerView = binding.listadoPartidos
        partidosRecyclerView.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        partidosRecyclerView.adapter = myAdapter

        partidosRecyclerView.setOnClickListener {
            cerrarTeclado()
        }

        binding.mainContainer.setOnClickListener{
            cerrarTeclado()
        }
        binding.aplicarFiltro.setOnClickListener {
            var equipo = binding.tfFiltro.text
            var filtroAplicado = aplicarFiltro(equipo.toString())
            if(filtroAplicado){
                myAdapter.applyFilter(partidosFiltrados)
            } else {
                myAdapter.applyFilter(todos.data)
            }

            cerrarTeclado()
        }
    }

    /**
     * Funcion para cerrar el teclado
     */
    private fun cerrarTeclado() {
        val input = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        input.hideSoftInputFromWindow(view?.getWindowToken(), 0)

    }

    /**
     * Aplicar filtro por equipos
     */
    private fun aplicarFiltro(equipo: String): Boolean {
        partidosFiltrados = arrayListOf()
        if (!(equipo == "")) {
            for (partido in todos.data) {
                if (partido.equipoLocal.nombre == equipo || partido.equipoVisitante.nombre == equipo) {
                    partidosFiltrados.add(partido)
                }
            }
            return true
        }
        return false
    }


    /**
     * Leer JSON
     */
    private fun readJsonFromFile(fileName: String): String {
        var json = ""
        try {
            val bufferedReader = BufferedReader(
                InputStreamReader(activity?.assets?.open(fileName))
            )
            val paramsBuilder = StringBuilder()
            var line: String? = bufferedReader.readLine()
            while (line != null) {
                paramsBuilder.append(line)
                line = bufferedReader.readLine()
            }
            json = paramsBuilder.toString()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return json
    }

}