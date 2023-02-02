package com.utad.integrador

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.utad.integrador.databinding.FragmentVistaDatosPartidoBinding
import com.utad.integrador.model.Partido


class VistaDatosPartido : Fragment() {
    private var _binding: FragmentVistaDatosPartidoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVistaDatosPartidoBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Toolbar
        val toolbar = binding.toolbar
        toolbar.title = ""
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }

        // Datos del partido
        val partido: Partido = arguments?.getSerializable("miPartido") as Partido

        Picasso.get().load(partido.equipoLocal.escudo).into(binding.imgEquipoLocal)
        Picasso.get().load(partido.equipoVisitante.escudo).into(binding.imgEquipoVisitante)

        binding.tvGolLocal.text = partido.golesLocal.toString()
        binding.tvGolVisitante.text = partido.golesVisitante.toString()

        binding.cantAmarillasLocal.text = partido.amarillasLocal.toString()
        binding.cantAmarillasVisitante.text = partido.amarillasVisitante.toString()

        binding.cantRojasLocal.text = partido.rojasLocal.toString()
        binding.cantRojasVisitante.text = partido.rojasVisitante.toString()

        binding.cantCornersLocal.text = partido.cornerLocal.toString()
        binding.cantCornersVisitante.text = partido.cornerVisitante.toString()

        binding.posesionLocal.text = partido.posesionLocal.toString() + "%"
        binding.tvPosesionVisitante.text = partido.posesionVisitante.toString() + "%"

        binding.cantTirosLocal.text = partido.tirosLocal.toString()
        binding.cantTirosVisitante.text = partido.tirosVisitante.toString()
    }


}