package com.example.google_map_sample.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicle")
data class Vehicle (
    @PrimaryKey
    var id : Int,
    var type: String,
    var lat : Float,
    var lng : Float,
    var bearing : Int,
    var image_url : String
)