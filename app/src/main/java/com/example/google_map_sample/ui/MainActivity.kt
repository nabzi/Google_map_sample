package com.example.google_map_sample.ui

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
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
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val mainViewModel : MainViewModel by viewModel()
    lateinit var mapView  : MapView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main)
        val adapter = VehicleAdapter()
        binding.rvVehicles.adapter = adapter

        mapView = binding.mapview
        subscribeUi(adapter)
        with(mapView) {
            // Initialise the MapView
            onCreate(null)
            MapsInitializer.initialize(applicationContext)
        }

    }
    private fun subscribeUi(adapter: VehicleAdapter) {
        mainViewModel.vehicleList?.observe(this, Observer { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    //show progress
                }
                Status.ERROR -> {
                    //hide progress
                }
                Status.SUCCESS -> {
                    adapter.submitList(resource.data)

                    mapView.getMapAsync{googleMap ->
                        googleMap?.let {
                                addMarkers(it, resource.data)
                        }
                    }

                }
            }
        })
    }
    private fun addMarkers(googleMap: GoogleMap, list: List<Vehicle>?) {
        if (list == null)
             return
        var carPos : LatLng 
        var bitmapList = arrayListOf<Bitmap>()

        GlobalScope.async(Dispatchers.Main) {
            val job = async(Dispatchers.IO) {
                for (vehicle : Vehicle in list.iterator()) {

                    val bitmap: Bitmap = Glide
                        .with(this@MainActivity)
                        .asBitmap()
                        .load(vehicle.image_url)
                        .submit()
                        .get()
                    bitmapList.add(bitmap)
                }
            }
            job.await();

           for ((index, vehicle) in list.iterator().withIndex()) {
               carPos = LatLng(vehicle.lat, vehicle.lng)
               googleMap.addMarker(
                   MarkerOptions().position(carPos).title("").rotation(vehicle.bearing.toFloat())
                       .icon(BitmapDescriptorFactory.fromBitmap(bitmapList[index]))
               )
           }

        }
        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(list[0].lat , list[0].lng), 17.0f
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