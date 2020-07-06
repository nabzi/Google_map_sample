package com.example.google_map_sample.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicle")
data class Vehicle (
    @PrimaryKey (autoGenerate = true)
    var id : Int,
    var type: String,
    var lat : Double,
    var lng : Double,
    var bearing : Int,
    var image_url : String
)