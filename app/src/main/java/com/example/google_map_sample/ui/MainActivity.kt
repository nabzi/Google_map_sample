package com.example.google_map_sample.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.google_map_sample.R
import com.example.google_map_sample.databinding.ActivityMainBinding
import com.example.google_map_sample.model.Status
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel : MainViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_main)
        val adapter = VehicleAdapter()
        binding.rvVehicles.adapter = adapter
        subscribeUi(adapter)
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
                }
            }
        })
    }
}