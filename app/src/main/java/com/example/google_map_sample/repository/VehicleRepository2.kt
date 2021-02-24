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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.collect
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
interface VehicleRepository2{
    suspend fun getList(): Flow<Resource<List<Vehicle>>>?
}
class VehicleRepositoryImpl2(private val apiService  : ApiService, val vehicleDao: VehicleDao )
      :VehicleRepository2 {

    override suspend fun getList(): Flow<Resource<List<Vehicle>>>? {
        return pullFromServer()
//        return flow {
//            pullFromServer()
//                .collect {
//                it.data?.let{ vehicleDao.addList(it)}
//                emit(it) }
//
//        }

    }

    private suspend fun pullFromServer(): Flow<Resource<List<Vehicle>>> {
        var result: Resource<List<Vehicle>> = Resource.loading(null)
        val resultFlow = MutableStateFlow(result)
       // withContext(Dispatchers.IO) {

//        coroutineScope.launch (Dispatchers.IO ) {
            try {
                val response = apiService.getVehicleList()
                if (response.isSuccessful)
                    result = Resource.success(response.body()?.vehicles)
                else
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
            resultFlow.emit(result)
            //}

       // }
        return resultFlow
    }

}