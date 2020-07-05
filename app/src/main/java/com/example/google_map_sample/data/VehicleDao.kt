package com.example.google_map_sample.data

import androidx.lifecycle.LiveData
import com.example.google_map_sample.model.Vehicle

@Dao
abstract class VehicleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg Vehicle: Vehicle)

    @Query("SELECT * FROM Vehicle " )
    abstract fun load(): LiveData<List<Vehicle>>
}