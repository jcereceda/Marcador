package com.utad.integrador

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.utad.integrador.api.ApiRest
import com.utad.integrador.databinding.FragmentVistaCompletarPartidoBinding
import com.utad.integrador.model.Equipo
import com.utad.integrador.model.Marcador
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VistaCompletarPartido : Fragment() {

    private var _binding: FragmentVistaCompletarPartidoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentVistaCompletarPartidoBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    private lateinit var sbPosesion: SeekBar
    private lateinit var tvPosLocal: TextView
    private lateinit var tvPosVisitante: TextView
    private lateinit var partidoActual: Marcador


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Instanciar partido
        partidoActual = Marcador(0,0,0,0,0,0,0,0,0,0,0,0,0,0)
        // Poner Equipos
        binding.tvEquipoLocal.text = arguments?.getString("local").toString()
        binding.tvEquipoVisitante.text = arguments?.getString("visitante").toString()
        partidoActual.equipoLocal = arguments?.getInt("idLocal")!!.toInt()
        partidoActual.equipoVisitante = arguments?.getInt("idVisitante")!!.toInt()

        // Listeners de botones para datos del partido
        binding.GolLocal.setOnClickListener {
            partidoActual.golesLocal++
            partidoActual.tirosLocal++
            binding.tvGolLocal.text = partidoActual.golesLocal.toString()
        }
        binding.GolVisitante.setOnClickListener {
            partidoActual.golesVisitante++
            partidoActual.tirosVisitante++
            binding.tvGolVisitante.text = partidoActual.golesVisitante.toString()
        }

        binding.RojaLocal.setOnClickListener {
            partidoActual.rojasLocal++
        }
        binding.RojaVisitante.setOnClickListener {
            partidoActual.rojasVisitante--
        }
        binding.AmarillaLocal.setOnClickListener {
            partidoActual.amarillasLocal++
        }
        binding.AmarillaVisitante.setOnClickListener {
            partidoActual.amarillasVisitante++
        }
        binding.cornerLocal.setOnClickListener {
            partidoActual.cornerLocal++
        }
        binding.cornerVisitante.setOnClickListener {
            partidoActual.cornerVisitante++
        }
        binding.tiroLocal.setOnClickListener {
            partidoActual.tirosLocal++
        }
        binding.tiroVisitante.setOnClickListener {
            partidoActual.tirosVisitante++
        }


        // Datos de la posesion
        sbPosesion = binding.sbPosesion
        tvPosLocal = binding.posesionLocal
        tvPosVisitante = binding.tvPosesionVisitante
        tvPosLocal.text = sbPosesion.progress.toString() + "%"
        tvPosVisitante.text = (100 - sbPosesion.progress).toString() + "%"

        sbPosesion.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvPosLocal.text = sbPosesion.progress.toString() + "%"
                tvPosVisitante.text = (100 - sbPosesion.progress).toString() + "%"
                partidoActual.posesionLocal = sbPosesion.progress
                partidoActual.posesionVisitante = (100 - sbPosesion.progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        // terminar partido y volver a la pagina anterior
        binding.terminar.setOnClickListener {
            // COnfirmar
            val builder: AlertDialog.Builder = AlertDialog.Builder(this.requireContext())
            builder.setTitle("Aviso")
            builder.setMessage("¿Quieres terminar el partido?")

            builder.setPositiveButton("Si") { dialog, which ->
                agregarPartido()
                Log.i("addPartido",partidoActual.toString())
                Toast.makeText(this.context, "Partido terminado y guardado", Toast.LENGTH_SHORT)
                    .show()
                activity?.supportFragmentManager?.popBackStack()
            }
            builder.setNegativeButton("No", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()

        }

        // Salir sin guardar
        // Toolbar
        val toolbar = binding.toolbar
        toolbar.title = ""
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this.requireContext())
            builder.setTitle("Alerta")
            builder.setMessage("Estas seguro que quieres salir? Perderás los datos no guardados")

            builder.setPositiveButton("Si") { dialog, which ->
                activity?.supportFragmentManager?.popBackStack()
            }
            builder.setNegativeButton("No", null)
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    private fun agregarPartido() {

        val call = ApiRest.service.addPartido(partidoActual)
        call.enqueue(object : Callback<Marcador> {
            override fun onResponse(call: Call<Marcador>, response: Response<Marcador>) {
                val body = response.body()
                if (response.isSuccessful && body != null) {
                    Log.i("VistaCompletarPartido", body.toString())
                } else {
                    Log.e("VistaCompletarPartido", response.errorBody()?.string() ?: "error")
                }


            }

            override fun onFailure(call: Call<Marcador>, t: Throwable) {
                Log.e("VistaCompletarPartido", t.message.toString())
            }

        })

    }
}