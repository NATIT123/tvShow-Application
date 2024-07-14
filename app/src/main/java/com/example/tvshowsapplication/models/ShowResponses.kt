package com.example.tvshowsapplication.models

data class ShowResponses(
    val page: Int,
    val pages: Int,
    val total: String,
    val tv_shows: List<TvShow>
)