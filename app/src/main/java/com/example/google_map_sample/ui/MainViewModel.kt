package com.example.google_map_sample.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.google_map_sample.repository.VehicleRepository
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainViewModel  (vehicleRepository: VehicleRepository) : ViewModel(){
    val vehicleList = vehicleRepository.getList(viewModelScope)

}