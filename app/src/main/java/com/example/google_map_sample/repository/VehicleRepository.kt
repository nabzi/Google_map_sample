package com.example.google_map_sample.repository

import androidx.lifecycle.LiveData
import com.example.google_map_sample.model.Resource
import com.example.google_map_sample.model.Vehicle
import com.example.google_map_sample.network.ApiService

class VehicleRepository (apiService  : ApiService) {
    fun getList(): LiveData<Resource<List<Vehicle>>>? {
        return null
    }

    fun insertTestData() {

    }
}