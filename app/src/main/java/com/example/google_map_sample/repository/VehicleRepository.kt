package com.example.google_map_sample.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.google_map_sample.data.VehicleDao
import com.example.google_map_sample.model.Resource
import com.example.google_map_sample.model.Status
import com.example.google_map_sample.model.Vehicle
import com.example.google_map_sample.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
interface VehicleRepository{
    fun getList(coroutineScope: CoroutineScope = GlobalScope): LiveData<Resource<List<Vehicle>>>?
}
class VehicleRepositoryImpl(val apiService  : ApiService , val vehicleDao: VehicleDao ) {
    fun getList(coroutineScope: CoroutineScope): LiveData<Resource<List<Vehicle>>>? {
        return object : NetworkBoundResource<List<Vehicle>, List<Vehicle>>(coroutineScope) {
            override fun onFetchFailed() {
                super.onFetchFailed()
            }

            override fun saveCallResult(items: List<Vehicle>) {
                coroutineScope.launch {
                    vehicleDao.addList(items)
                }
            }

            override fun shouldFetch(data: List<Vehicle>?): Boolean {
                return data == null || data.isEmpty()
            }

            override fun loadFromDb(): LiveData<List<Vehicle>> {
                return vehicleDao.loadVehicleList()
            }

            override fun createCall(): LiveData<Resource<List<Vehicle>>> {
                return pullFromServer(coroutineScope)
            }
        }.asLiveData()
    }

    private fun pullFromServer(coroutineScope: CoroutineScope): LiveData<Resource<List<Vehicle>>> {
        var result : Resource<List<Vehicle>>
        val resultLiveData = MutableLiveData<Resource<List<Vehicle>>>()
        coroutineScope.launch (Dispatchers.IO ) {
            try {
                val response = apiService.getVehicleList()
               // Log.d("LOGTAG1", response.body().toString())
                if(response.isSuccessful)
                    result = Resource.success(response.body()?.vehicles)
                else
                    result = Resource(Status.ERROR, null, "error")
            } catch (exception: HttpException) {
                result = Resource(Status.ERROR, null, exception.code().toString().plus(exception.message()))
            } catch (exception: ConnectException) {
                result = Resource(Status.ERROR, null, "ERR_CONNECTION")
            } catch (exception: SocketTimeoutException) {
                result = Resource(Status.ERROR, null, "ERR_TIMEOUT")
            } catch (exception: Exception) {
                result = Resource(Status.ERROR, null, exception.message)
            }
            resultLiveData.postValue(result)
        }
        return resultLiveData
    }


    fun insertTestData() {

    }
}