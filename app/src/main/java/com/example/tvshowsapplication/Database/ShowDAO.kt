package com.example.tvshowsapplication.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tvshowsapplication.models.TvShow
import com.example.tvshowsapplication.models.TvShowDetail

@Dao
interface ShowDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(tvShow: TvShow): Long

    @Query("SELECT * FROM tvShow")
    fun getListArticle(): LiveData<List<TvShow>>

    @Delete
    suspend fun deleteTvShow(tvShow: TvShow)
}