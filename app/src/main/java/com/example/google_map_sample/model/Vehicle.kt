package com.example.google_map_sample.model

@Entity
data class Vehicle (
    @PrimaryKey
    var id : Int,
    var type: String,
    var lat : Float,
    var lng : Float,
    var bearing : Int,
    var image_url : String
)