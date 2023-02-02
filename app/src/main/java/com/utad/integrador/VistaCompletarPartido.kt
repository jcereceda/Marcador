package com.utad.integrador

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.utad.integrador.databinding.FragmentVistaCompletarPartidoBinding
import com.utad.integrador.model.Partido


class VistaCompletarPartido : Fragment() {

    private var _binding: FragmentVistaCompletarPartidoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVistaCompletarPartidoBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    private lateinit var sbPosesion: SeekBar
    private lateinit var tvPosLocal: TextView
    private lateinit var tvPosVisitante: TextView
    private lateinit var partidoActual: Partido

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Instanciar partido
        partidoActual = Partido()
        // Poner Equipos
        binding.tvEquipoLocal.text = arguments?.getString("local").toString()
        binding.tvEquipoVisitante.text = arguments?.getString("visitante").toString()
        partidoActual.equipoLocal.nombre = binding.tvEquipoLocal.text as String
        partidoActual.equipoVisitante.nombre = binding.tvEquipoVisitante.text as String

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
}