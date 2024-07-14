package com.example.tvshowsapplication.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.tvshowsapplication.models.TvShow
import com.example.tvshowsapplication.models.TvShowDetail

@Database(entities = [TvShow::class], version = 1)
@TypeConverters(ShowTypeConvertor::class)
abstract class ShowDatabase : RoomDatabase() {
    companion object {
        private var INSTANCE: ShowDatabase? = null
        private val DATABASE_NAME = "show.db"

        @Synchronized
        fun getInstance(context: Context): ShowDatabase {
            if (INSTANCE == null) {
                synchronized(ShowDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(
                            context.applicationContext,
                            ShowDatabase::class.java,
                            DATABASE_NAME
                        )
                            .allowMainThreadQueries()
                            .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }

    abstract fun showDao(): ShowDAO
}