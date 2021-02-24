package com.example.google_map_sample

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.google_map_sample.data.DB
import com.example.google_map_sample.network.ApiService
import com.example.google_map_sample.network.AppRetrofit
import com.example.google_map_sample.repository.VehicleRepository
import com.example.google_map_sample.repository.VehicleRepository2
import com.example.google_map_sample.repository.VehicleRepositoryImpl
import com.example.google_map_sample.repository.VehicleRepositoryImpl2
import com.example.google_map_sample.ui.MainViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MyApp : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            // declare used Android context
            androidContext(this@MyApp)
            // declare modules
            modules(myModule)
        }
    }

}
val myModule = module {
    single<VehicleRepository>{ VehicleRepositoryImpl(get(),get())}
    single<VehicleRepository2>{ VehicleRepositoryImpl2(get(),get()) }
    single{ DB.get(androidContext()) }
    single{ AppRetrofit.getInstance().create(ApiService::class.java)}
    single{ get<DB>().vehicleDao() }
    viewModel { MainViewModel(get()) }
}