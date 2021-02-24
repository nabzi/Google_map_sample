package com.example.google_map_sample.ui

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.google_map_sample.model.Resource
import com.example.google_map_sample.model.Vehicle
import com.example.google_map_sample.repository.VehicleRepository
import com.example.google_map_sample.repository.VehicleRepository2
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel  (vehicleRepository: VehicleRepository2) : ViewModel() {
    var vehicleList : Flow<Resource<List<Vehicle>>>? =  vehicleRepository.getList()

fun getTmp() : List<Int>{
    return listOf(1, 2, 3)
}
    fun loadBitmapList(context: Context? ,  list: List<Vehicle>): LiveData<List<Bitmap>> {
        var bitmapList = arrayListOf<Bitmap>()
        var liveData = MutableLiveData<List<Bitmap>>()
        viewModelScope.launch (context = Dispatchers.IO) {
            context?.let {
                for (vehicle: Vehicle in list.iterator()) {
                    val bitmap: Bitmap = Glide
                        .with(it)
                        .asBitmap()
                        .load(vehicle.image_url)
                        .submit()
                        .get()
                    bitmapList.add(bitmap)
                }
                liveData.postValue(bitmapList)
            }
        }
        return liveData
    }
}