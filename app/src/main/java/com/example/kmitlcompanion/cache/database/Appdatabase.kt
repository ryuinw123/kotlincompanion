package com.example.kmitlcompanion.cache.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kmitlcompanion.cache.dao.CachedDao
import com.example.kmitlcompanion.cache.entities.DataProperty
import com.example.kmitlcompanion.cache.entities.MapPointEntity
import com.example.kmitlcompanion.cache.entities.NotiLogEntity
import com.example.kmitlcompanion.cache.entities.User
import javax.inject.Inject

@Database(
    entities = [
        MapPointEntity::class,
        DataProperty::class,
        User::class,
        NotiLogEntity::class,
    ],
    version = 1
)
abstract class AppDatabase  : RoomDatabase() {

    abstract fun cachedDao(): CachedDao

    companion object {

        private var INSTANCE: AppDatabase? = null
        private val lock = Any()

        fun getInstance(context: Context): AppDatabase {
            if (INSTANCE == null) {
                synchronized(lock) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java, "projects.db"
                        ).build()
                    }
                    return INSTANCE as AppDatabase
                }
            }
            return INSTANCE as AppDatabase
        }
    }

}