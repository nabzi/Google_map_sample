package com.example.google_map_sample.network
import com.example.google_map_sample.model.Vehicle
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("/assets/test/document.json")
    suspend fun getVehicleList() : Response<Vehicles>

}
data class Vehicles(
    var vehicles : List<Vehicle>
)