package com.example.google_map_sample.data



import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.google_map_sample.model.Vehicle
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Main database description.
 */
@Database(
    entities = [
      Vehicle::class],
    version = 1,
    exportSchema = false
)
abstract class DB : RoomDatabase() {

    abstract fun vehicleDao(): VehicleDao

    companion object {
        private var instance: DB? = null

        @Synchronized
        fun get(context: Context): DB {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    DB::class.java, "CheeseDatabase"
                )
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            fillInDb(context.applicationContext)
                        }
                    }).build()
            }
            return instance!!
        }

        /**
         * fill database with list of cheeses
         */
        private fun fillInDb(context: Context) {
            // inserts in Room are executed on the current thread, so we insert in the background
            GlobalScope.launch {

                //get(context).feedDao().insert(Feed(3,"st3"))
            }
        }
    }
}
