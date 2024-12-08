package com.example.garbagehub.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.garbagehub.data.models.*

@Database(
    entities = [
        GarbageBin::class,
        User::class,
        BinReport::class,
        MaintenanceTask::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class GarbageHubDatabase : RoomDatabase() {
    abstract fun binDao(): BinDao
    abstract fun userDao(): UserDao
    abstract fun reportDao(): ReportDao
    abstract fun maintenanceDao(): MaintenanceDao

    companion object {
        @Volatile
        private var INSTANCE: GarbageHubDatabase? = null

        fun getDatabase(context: Context): GarbageHubDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GarbageHubDatabase::class.java,
                    "garbagehub_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
