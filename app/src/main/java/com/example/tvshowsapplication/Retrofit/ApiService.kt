package com.example.tvshowsapplication.Retrofit

import com.example.tvshowsapplication.models.ShowResponses
import com.example.tvshowsapplication.Utils.Constants.Companion.BASE_URL
import com.example.tvshowsapplication.models.DetailShowResponses
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    companion object {
        private val gson: Gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()

        val apiService: ApiService by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(logging).build()
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(
                GsonConverterFactory.create(
                    gson
                )
            ).client(client).build().create(ApiService::class.java)
        }
    }

    @GET("most-popular")
    fun getPopularShows(@Query("page") page: Int = 1): Call<ShowResponses>

    @GET("search")
    fun searchShow(@Query("q") searchQuery: String): Call<ShowResponses>


    @GET("show-details")
    fun showDetailShow(@Query("q") idShow: Int): Call<DetailShowResponses>
}