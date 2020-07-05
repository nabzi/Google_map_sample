package com.example.google_map_sample.ui

import androidx.lifecycle.ViewModel
import com.example.google_map_sample.repository.VehicleRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModel  (vehicleRepository: VehicleRepository) : ViewModel(){
    // The Int type argument corresponds to a PositionalDataSource object.
    val vehicleList = vehicleRepository.getList()
    init{
        GlobalScope.launch {
            vehicleRepository.insertTestData()
        }
    }
}