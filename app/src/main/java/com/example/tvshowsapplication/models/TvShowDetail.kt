package com.example.tvshowsapplication.models

import androidx.room.Entity
import androidx.room.PrimaryKey


data class TvShowDetail(
    val countdown: Any?,
    val country: String,
    val description: String,
    val description_source: String,
    val end_date: Any?,
    val episodes: List<Episode>,
    val genres: List<String>,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val image_path: String,
    val image_thumbnail_path: String,
    val name: String,
    val network: String,
    val permalink: String,
    val pictures: List<String>,
    val rating: String,
    val rating_count: String,
    val runtime: Int,
    val start_date: String,
    val status: String,
    val url: String,
    val youtube_link: Any?
)