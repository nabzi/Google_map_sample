package com.example.google_map_sample.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.google_map_sample.model.Vehicle

@Dao
abstract class VehicleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun add(vararg Vehicle: Vehicle)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun addList(list: List<Vehicle>)

    @Query("SELECT * FROM vehicle " )
    abstract fun loadVehicleList(): LiveData<List<Vehicle>>
}