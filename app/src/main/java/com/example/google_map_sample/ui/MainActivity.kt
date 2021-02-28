package com.example.google_map_sample.ui

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.google_map_sample.R
import com.example.google_map_sample.databinding.ActivityMainBinding
import com.example.google_map_sample.model.Status
import com.example.google_map_sample.model.Vehicle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import org.koin.android.viewmodel.ext.android.viewModel


/*

In this sample code,a list of cars is fetched by network call and shown both in a list and a map view.
The code is in Kotlin language , and the architecture used is MVVM. Repository pattern is used to feed data to viewmodel.
Hear is a list of tools used  :
- Room , ViewModel and Livedata , along with databinding
- Kotlin coroutines for asyncronous operations such as network call, fetching resources and inserting in database
- Koin lightweight dependency injection framework for Kotlin
- Mapview for showing google map in activity
- ListAdapter for showing data optimally in a RecyclerView

Author : Zeinab Modir
zeinab.modir@gmail.com

*/

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()
    lateinit var mapView: MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )
        val adapter = VehicleAdapter()
        binding.rvVehicles.adapter = adapter

        mapView = binding.mapview
        subscribeUi(adapter)

        // Initialise the MapView
        mapView.onCreate(null)
        MapsInitializer.initialize(applicationContext)

    }

    private fun subscribeUi(adapter: VehicleAdapter) {
        this.lifecycleScope.launch {
            mainViewModel.vehicleList?.collect { resource
                ->
                resource?.data?.let {
                    adapter.submitList(it)

                    mapView.getMapAsync { googleMap ->
                        googleMap?.let { map ->
                            addMarkers(map, it)
                        }
                    }
                }
//                when (resource?.status) {
//                    Status.LOADING -> {
//                        //show progress
//                    }
//                    Status.ERROR -> {
//                        //hide progress
//                    }
//                    Status.SUCCESS -> {
//                        adapter.submitList(resource.data)
//
//                        mapView.getMapAsync { googleMap ->
//                            googleMap?.let {
//                                addMarkers(it, resource.data)
//                            }
//                        }
//
//                    }
//                }
            }
        }
    }

    private fun addMarkers(googleMap: GoogleMap, list: List<Vehicle>?) {
        if (list == null || list.isEmpty())
            return
        var carPos: LatLng
        mainViewModel.loadBitmapList(this.baseContext, list).observe(this, Observer { bitmapList ->
            for ((index, vehicle) in list.iterator().withIndex()) {
                carPos = LatLng(vehicle.lat, vehicle.lng)
                googleMap.addMarker(
                    MarkerOptions().position(carPos).title("").rotation(vehicle.bearing.toFloat())
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmapList[index]))
                )
            }
        })
        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(list[0].lat, list[0].lng), 17.0f
            )
        )
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

}