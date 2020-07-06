package com.example.google_map_sample

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.google_map_sample.ui.MainViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.google_map_sample.model.Resource
import com.example.google_map_sample.model.Status
import com.example.google_map_sample.model.Vehicle
import com.example.google_map_sample.repository.VehicleRepository
import com.nhaarman.mockitokotlin2.any
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
fun <T> any(type: Class<T>): T = Mockito.any<T>(type)
class MainViewModelTest{

    var repo : VehicleRepository = Mockito.mock(VehicleRepository::class.java)
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
    fun test_vehicle_list_load_success() {
            var result = MutableLiveData<Resource<List<Vehicle>>>()
            result.postValue(Resource.success(list))
            `when`<Any>(repo.getList()).thenReturn(result)
            var mainViewModel = MainViewModel(repo)
            mainViewModel.vehicleList?.observeForever{
                println(it.status)
            }
            assertEquals(list.size,  mainViewModel.vehicleList?.value?.data?.size)
            assertEquals(1 , mainViewModel.vehicleList?.value?.data?.get(0)?.id )
    }
    @Test
    fun test_vehicle_list_load_error() {
        var result = MutableLiveData<Resource<List<Vehicle>>>()
        result.postValue(Resource.error("error1" , null))
        `when`<Any>(repo.getList()).thenReturn(result)
        var mainViewModel = MainViewModel(repo)
        mainViewModel.vehicleList?.observeForever{
            println(it.status)
        }
        assertEquals(Status.ERROR,  mainViewModel.vehicleList?.value?.status)
    }

}