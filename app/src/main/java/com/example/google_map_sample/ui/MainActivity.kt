package com.example.google_map_sample.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.example.google_map_sample.R
import com.example.google_map_sample.databinding.ActivityMainBinding
import com.example.google_map_sample.model.Status
import com.example.google_map_sample.model.Vehicle
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.koin.android.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val mainViewModel : MainViewModel by viewModel()
    lateinit var mapView  : MapView
    var carList  : List<Vehicle>? = null
    var googleMap : GoogleMap? = null
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
                    carList = resource.data
                    mapView.getMapAsync{
                        //setMapLocation(it, position)
                        googleMap = it
                        googleMap?.let { mMap ->
                            val sydney = LatLng(-34.0, 151.0)
                            mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
                        }
                    }

                }
            }
        })
    }
    val position = LatLng(-33.920455, 18.466941)
    private fun setMapLocation(map : GoogleMap , position: LatLng) {
//        with(map) {
//            moveCamera(CameraUpdateFactory.newLatLngZoom(position, 13f))
//            addMarker(MarkerOptions().position(position))
//            mapType = GoogleMap.MAP_TYPE_NORMAL
//            setOnMapClickListener {
//                Toast.makeText(this@MainActivity, "Clicked on map", Toast.LENGTH_SHORT).show()
//            }
//        }
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