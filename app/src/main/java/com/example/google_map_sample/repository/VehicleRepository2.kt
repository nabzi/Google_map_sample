package com.example.google_map_sample.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.google_map_sample.data.VehicleDao
import com.example.google_map_sample.model.Resource
import com.example.google_map_sample.model.Status
import com.example.google_map_sample.model.Vehicle
import com.example.google_map_sample.network.ApiService
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException

interface VehicleRepository2 {
    fun getList(
        shouldFetch: Boolean,
        coroutineScope: CoroutineScope
    ): MutableStateFlow<Resource<List<Vehicle>>?>
}

class VehicleRepositoryImpl2(private val apiService: ApiService, val vehicleDao: VehicleDao) :
    VehicleRepository2 {

    override fun getList(
        shouldFetch: Boolean,
        coroutineScope: CoroutineScope
    ): MutableStateFlow<Resource<List<Vehicle>>?> {
        var dbData: List<Vehicle>? = null
        var stateFlow: MutableStateFlow<Resource<List<Vehicle>>?> = MutableStateFlow(null)
        var dataFlow = if (shouldFetch)
            vehicleDao.loadVehicleListFLow().map {
                dbData = it
                Resource.loading(it)
            }
        else
            vehicleDao.loadVehicleListFLow().map {
                dbData = it
                Resource.success(it)
            }
        coroutineScope.launch {
            dataFlow.collect {
                stateFlow.emit(it)
            }
        }
        if (shouldFetch)
            coroutineScope.launch(Dispatchers.IO) {
                val resource = pullFromServer()
                stateFlow.emit(
                    if (resource.status == Status.ERROR) {
                        Resource.error<List<Vehicle>>(
                            resource.message ?: "error loading from server", dbData
                        )
                    } else
                        Resource.success(resource.data)
                )
            }
        return stateFlow
    }

    private suspend fun pullFromServer(): Resource<List<Vehicle>> {
        var result: Resource<List<Vehicle>> = Resource.loading(null)
        try {
            val response = apiService.getVehicleList()
            if (response.isSuccessful) {
                result = Resource.success(response.body()?.vehicles)
                response.body()?.vehicles?.let {
                    //clear table if entity doesn't have id
                    vehicleDao.addList(it)
                }
            } else
                result = Resource(Status.ERROR, null, "error")
        } catch (exception: HttpException) {
            result = Resource(
                Status.ERROR,
                null,
                exception.code().toString().plus(exception.message())
            )
        } catch (exception: ConnectException) {
            result = Resource(Status.ERROR, null, "ERR_CONNECTION")
        } catch (exception: SocketTimeoutException) {
            result = Resource(Status.ERROR, null, "ERR_TIMEOUT")
        } catch (exception: Exception) {
            result = Resource(Status.ERROR, null, exception.message)
        }
        return result
    }

}