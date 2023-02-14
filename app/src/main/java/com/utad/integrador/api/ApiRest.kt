package com.utad.integrador.api


import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiRest {
    lateinit var service: ApiService
    // Movil con wifi del movil
    // val URL =  "http://192.168.244.246:8080/api/"
    // Desde casa con el movil
    //val URL = "http://192.168.1.51:8080/api/"

    // localhost con emulador
    val URL = "http://10.0.2.2:8080/api/"
    val language = "es-ES"


    fun initService() {
        val retrofit = Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(ApiService::class.java)
    }
}