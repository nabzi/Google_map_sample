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

/*
 * {
"vehicles":[
{
"type": "ECO",
"lat": 35.7575154,
"lng": 51.4104956,
"bearing": 54,
"image_url":"https://snapp.ir/assets/test/snapp_map@2x.png"
},
{
"type": "ECO",
"lat": 35.7580966,
"lng": 51.4094662,
"bearing": 205,
"image_url":"https://snapp.ir/assets/test/snapp_map@2x.png"
},
{
"type": "PLUS",
"lat": 35.7577213,
"lng": 51.4092553,
"bearing": 324,
"image_url":"https://snapp.ir/assets/test/snapp_map_st2.png"
},
{
"type": "ECO",
"lat": 35.7571681,
"lng": 51.4091973,
"bearing": 287,
"image_url":"https://snapp.ir/assets/test/snapp_map@2x.png"
},
{
"type": "PLUS",
"lat": 35.7570448,
"lng": 51.4092871,
"bearing": 168,
"image_url":"https://snapp.ir/assets/test/snapp_map_st2.png"
}
]
}
 *
 */