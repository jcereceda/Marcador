package com.utad.integrador

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.utad.integrador.Adapter.PartidosAdapter
import com.utad.integrador.api.ApiRest
import com.utad.integrador.databinding.FragmentVistaListadoPartidosBinding
import com.utad.integrador.model.Equipo
import com.utad.integrador.model.Marcador
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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

   // private lateinit var todos: PartidosRespose
    private lateinit var partidosFiltrados: ArrayList<Marcador>
    var data: ArrayList<Marcador> = ArrayList()
    var listEquipos: ArrayList<Equipo> = ArrayList()
    var loader: ProgressBar? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        callGetPartidos()
        callGetEquipo()
        loader = view.findViewById(R.id.progreso)
       // val json = readJsonFromFile("partidos.json")
        // todos = Gson().fromJson(json, PartidosRespose::class.java)

        val myAdapter = PartidosAdapter(data, listEquipos) {

            val fragment = VistaDatosPartido()
            val bundle = Bundle()
            bundle.putSerializable("miPartido", it)
            val x = it.equipoVisitante
            val y = it.equipoLocal
            val elocal = listEquipos.filter { it.id == y }.single()
            val eVisitante = listEquipos.filter { it.id == x }.single()
            bundle.putSerializable("local",elocal)
            bundle.putSerializable("visitante",eVisitante)

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
                myAdapter.applyFilter(data)
            }

            cerrarTeclado()
        }
    }

    // Funcion para traer los equipos

    private fun callGetEquipo() {
        val call = ApiRest.service.getEquipos()
        call.enqueue(object : Callback<List<Equipo>> {
            override fun onResponse(call: Call<List<Equipo>>, response: Response<List<Equipo>>) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Log.i("VistaNuevoPartido", body.toString())
                    listEquipos.clear()
                    listEquipos.addAll(body)
                    Log.i("vistaListadoPartidos",listEquipos.toString())

                } else {
                    Log.e("VistaNuevoPartido", response.errorBody()?.string() ?: "error")
                }

                loader?.isVisible = false
                //swipeRefreshLayout.isRefreshing = false
            }

            override fun onFailure(call: Call<List<Equipo>>, t: Throwable) {
                Log.e("VistaNuevoPartido4", t.message.toString())
                loader?.isVisible = false
                // swipeRefreshLayout.isRefreshing= false
            }

        })
    }

    // Funcion para traer los partidos
    private fun callGetPartidos() {
        val call = ApiRest.service.getPartidos()
        call.enqueue(object : Callback<List<Marcador>> {
            override fun onResponse(call: Call<List<Marcador>>, response: Response<List<Marcador>>) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Log.i("vistaListadoPartidos", body.toString())
                    data.clear()
                    data.addAll(body)
                    Log.i("vistaListadoPartidos1", data.toString())

                } else {
                    Log.e("vistaListadoPartidos", response.errorBody()?.string() ?: "error")
                }

                //loader?.isVisible = false
                //swipeRefreshLayout.isRefreshing = false
            }

            override fun onFailure(call: Call<List<Marcador>>, t: Throwable) {
                Log.e("vistaListadoPartidos", t.message.toString())
                // loader?.isVisible = false
                // swipeRefreshLayout.isRefreshing= false
            }

        })
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
            var idLocal: Equipo = listEquipos.filter { it.nombre == equipo }.single()
            var idVisitante: Equipo = listEquipos.filter { it.nombre == equipo }.single()
            for (partido in data) {
                if (partido.equipoLocal == idLocal.id || partido.equipoVisitante == idVisitante.id) {
                    partidosFiltrados.add(partido)
                }
            }
            return true
        }
        return false
    }


}