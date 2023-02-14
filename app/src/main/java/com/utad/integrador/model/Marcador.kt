package com.utad.integrador.model

data class Marcador(
    //var id: Int,
    var equipoLocal: Int,
    var equipoVisitante: Int,
    var amarillasLocal: Int,
    var amarillasVisitante: Int,
    var cornerLocal: Int,
    var cornerVisitante: Int,
    var golesLocal: Int,
    var golesVisitante: Int,
    var posesionLocal: Int,
    var posesionVisitante: Int,
    var rojasLocal: Int,
    var rojasVisitante: Int,
    var tirosLocal: Int,
    var tirosVisitante: Int
): java.io.Serializable {

   init{
       equipoLocal = 0
       equipoVisitante = 0
       amarillasLocal = 0
       amarillasVisitante = 0
       cornerLocal = 0
       cornerVisitante = 0
       golesLocal = 0
       golesVisitante = 0
       posesionLocal = 50
       posesionVisitante = 50
       rojasLocal = 0
       rojasVisitante = 0
       tirosLocal = 0
       tirosVisitante = 0
   }
}