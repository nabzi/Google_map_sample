package com.example.google_map_sample

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
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
    viewModel { MainViewModel(get()) }
    single { VehicleRepository(get()) }
    single{
        DB.get(androidContext())
    }
    single { get<DB>().feedDao() }
}