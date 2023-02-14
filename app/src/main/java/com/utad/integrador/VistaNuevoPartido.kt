package com.utad.integrador

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.squareup.picasso.Picasso
import com.utad.integrador.api.ApiRest
import com.utad.integrador.model.Equipo
import com.utad.integrador.model.EquiposResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VistaNuevoPartido : Fragment()  {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vista_nuevo_partido, container, false)
    }

    private var equipoLocal: String? = null
    private var equipoVisitante: String? = null
    private lateinit var tvError: TextView



    var data: ArrayList<Equipo> = ArrayList()
    var arrayEquipos = Array<String>(20){""}
    var nombresEquipos = arrayListOf<String>()
    lateinit var spinnerLocal: Spinner
    lateinit var spinnerVisitante: Spinner
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/b/bd/Logo_Primera_RFEF.png").into(view.findViewById<ImageView>(R.id.logo))

        // Iniciar api y poner los equipos
        ApiRest.initService()
        callGetEquipo()


        var etLocal = view.findViewById<TextView>(R.id.etLocal)
        var etVisitante = view.findViewById<TextView>(R.id.etVisitante)

        etLocal.setOnClickListener {
            this.context?.let {
                MaterialAlertDialogBuilder(it)
                    .setTitle("escoge equipo")
                    .setItems(arrayEquipos) { dialog, which ->
                        equipoLocal = arrayEquipos[which]
                        etLocal.text = equipoLocal
                    }
                    .show()
            }
        }

        etVisitante.setOnClickListener {
            this.context?.let {
                MaterialAlertDialogBuilder(it)
                    .setTitle("escoge equipo")
                    .setItems(arrayEquipos) { dialog, which ->
                        equipoVisitante = arrayEquipos[which]
                        etVisitante.text = equipoVisitante
                    }
                    .show()
            }
        }


        val btnEmpezar = view.findViewById<Button>(R.id.empezar)
        tvError = view.findViewById<TextView>(R.id.tvError)



        btnEmpezar.setOnClickListener {
            if(equipoLocal == equipoVisitante){
                tvError.text = "No pueden ser el mismo equipo"
            } else {
                tvError.text = ""
                var fragment = VistaCompletarPartido()
                val bundle = Bundle()
                bundle.putString("local", equipoLocal)
                bundle.putString("visitante", equipoVisitante)


                for (equipo: Equipo in data){
                    if(equipo.nombre == equipoLocal){
                        bundle.putInt("idLocal",equipo.id)
                    }
                }
                for (equipo: Equipo in data){
                    if(equipo.nombre == equipoVisitante){
                        bundle.putInt("idVisitante",equipo.id)
                    }
                }
                fragment.arguments = bundle
                activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.main_container, fragment)?.addToBackStack("VistaNuevoPartido")?.commit()

            }
        }
    }


    // FUncion para traer los equips de la api
    private fun callGetEquipo() {
        val call = ApiRest.service.getEquipos()
        call.enqueue(object : Callback<List<Equipo>> {
            override fun onResponse(call: Call<List<Equipo>>, response: Response<List<Equipo>>) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Log.i("VistaNuevoPartido", body.toString())
                    data.clear()
                    data.addAll(body)
                    Log.i("VistaNuevoPartido1",data.toString())
                    var i = 0
                    for (equipo: Equipo in data) {
                        arrayEquipos.set(i,equipo.nombre)
                        i++
                        nombresEquipos.add(equipo.nombre)
                    }
                Log.i("Main",nombresEquipos.toString())
                } else {
                    Log.e("VistaNuevoPartido", response.errorBody()?.string() ?: "error")
                }

                //loader?.isVisible = false
                //swipeRefreshLayout.isRefreshing = false
            }

            override fun onFailure(call: Call<List<Equipo>>, t: Throwable) {
                Log.e("VistaNuevoPartido4", t.message.toString())
                // loader?.isVisible = false
                // swipeRefreshLayout.isRefreshing= false
            }

        })
    }

}