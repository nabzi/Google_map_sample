package com.example.google_map_sample.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.google_map_sample.R
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel : MainViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}