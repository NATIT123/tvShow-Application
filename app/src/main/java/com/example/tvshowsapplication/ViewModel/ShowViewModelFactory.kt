package com.example.tvshowsapplication.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tvshowsapplication.Database.ShowDatabase

class ShowViewModelFactory(private val showDatabase: ShowDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ShowViewModel(showDatabase) as T
    }
}