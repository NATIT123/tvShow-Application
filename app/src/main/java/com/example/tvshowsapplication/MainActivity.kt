package com.example.tvshowsapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.tvshowsapplication.Database.ShowDatabase
import com.example.tvshowsapplication.ViewModel.ShowViewModel
import com.example.tvshowsapplication.ViewModel.ShowViewModelFactory
import com.example.tvshowsapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController


    val viewModel: ShowViewModel by lazy {
        val showDatabase = ShowDatabase.getInstance(this)
        val showViewModelFactory = ShowViewModelFactory(showDatabase)
        ViewModelProvider(this, showViewModelFactory)[ShowViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container_frame) as NavHostFragment

        navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.bottomNav, navController)


    }
}