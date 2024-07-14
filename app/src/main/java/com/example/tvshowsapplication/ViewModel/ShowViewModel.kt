package com.example.tvshowsapplication.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tvshowsapplication.Database.ShowDatabase
import com.example.tvshowsapplication.models.ShowResponses
import com.example.tvshowsapplication.models.TvShow
import com.example.tvshowsapplication.Retrofit.ApiService
import com.example.tvshowsapplication.models.DetailShowResponses
import com.example.tvshowsapplication.models.Episode
import com.example.tvshowsapplication.models.TvShowDetail
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShowViewModel(private val showDatabase: ShowDatabase) : ViewModel() {
    private var showPopularLiveData = MutableLiveData<List<TvShow>>()
    private var showDetailLiveData = MutableLiveData<TvShowDetail>()
    private var showSearchLiveData = MutableLiveData<List<TvShow>>()
    private var showFavoriteLiveData = showDatabase.showDao().getListArticle()


    fun getPopularShow(page: Int) {
        ApiService.apiService.getPopularShows(page).enqueue(object : Callback<ShowResponses> {
            override fun onResponse(call: Call<ShowResponses>, response: Response<ShowResponses>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        showPopularLiveData.postValue(body.tv_shows)
                    }
                }
            }

            override fun onFailure(p0: Call<ShowResponses>, p1: Throwable) {

            }

        })
    }

    fun saveTvShow(tvShow: TvShow) {
        viewModelScope.launch {
            showDatabase.showDao().upsert(tvShow)
        }
    }

    fun deleteTvShow(tvShow: TvShow) {
        viewModelScope.launch {
            showDatabase.showDao().deleteTvShow(tvShow)
        }
    }

    fun getDetailShow(idShow: Int) {
        ApiService.apiService.showDetailShow(idShow)
            .enqueue(object : Callback<DetailShowResponses> {
                override fun onResponse(
                    call: Call<DetailShowResponses>,
                    response: Response<DetailShowResponses>
                ) {
                    if (response.isSuccessful) {
                        val body = response.body()
                        if (body != null) {
                            showDetailLiveData.postValue(body.tvShow)
                        }
                    }
                }

                override fun onFailure(p0: Call<DetailShowResponses>, p1: Throwable) {

                }

            })
    }

    fun searchShow(searchQuery: String) {
        ApiService.apiService.searchShow(searchQuery).enqueue(object : Callback<ShowResponses> {
            override fun onResponse(call: Call<ShowResponses>, response: Response<ShowResponses>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        showSearchLiveData.postValue(body.tv_shows)
                    }
                }
            }

            override fun onFailure(p0: Call<ShowResponses>, p1: Throwable) {

            }

        })
    }

    fun observerShowPopularLiveData(): MutableLiveData<List<TvShow>> {
        return showPopularLiveData
    }

    fun observerDetailShowLiveData(): MutableLiveData<TvShowDetail> {
        return showDetailLiveData
    }

    fun observerFavoriteShowLiveData(): LiveData<List<TvShow>> {
        return showFavoriteLiveData
    }

    fun observerSearchShowLiveData(): MutableLiveData<List<TvShow>> {
        return showSearchLiveData
    }
}