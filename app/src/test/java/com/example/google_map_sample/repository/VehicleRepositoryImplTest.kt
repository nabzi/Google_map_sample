package com.example.google_map_sample.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.google_map_sample.data.VehicleDao
import com.example.google_map_sample.model.Resource
import com.example.google_map_sample.model.Vehicle
import com.example.google_map_sample.network.ApiService
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class VehicleRepositoryImplTest{
    
    var apiService = Mockito.mock(ApiService::class.java)
    var vehicleDao = Mockito.mock(VehicleDao::class.java)
    var list  = arrayListOf<Vehicle>()

    @Rule
    @JvmField
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup(){
        list.add(Vehicle(1 , "1" , 0.0,0.0,0,""))
        list.add(Vehicle(2 , "2" , 0.0,0.0,0,""))
    }
    @Test
    fun testPullFromServer() = runBlocking{
        var result = MutableLiveData<Resource<List<Vehicle>>>()
        result.postValue(Resource.success(list))
        Mockito.`when`<Any>(apiService.getVehicleList()).thenReturn(result)
        Mockito.`when`<Any>(vehicleDao.loadVehicleList()).thenReturn(null)
        var repo = VehicleRepositoryImpl(apiService,vehicleDao)
        var livedata = repo.getList()
        livedata?.observeForever{
            println(it.status)
        }
        assertEquals(list.size,  livedata?.value?.data?.size)
    }

}