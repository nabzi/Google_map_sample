package com.example.google_map_sample.network
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("/assets/test/document.json")
    suspend fun getVehicleList()

}