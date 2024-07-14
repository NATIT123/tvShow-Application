package com.example.tvshowsapplication.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "tvShow")
data class TvShow(
    val country: String,
    val end_date: Any?,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val image_thumbnail_path: String,
    val name: String,
    val network: String,
    val permalink: String,
    val start_date: String,
    val status: String
) : Serializable