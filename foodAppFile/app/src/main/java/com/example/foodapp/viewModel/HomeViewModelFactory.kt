package com.example.foodapp.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.foodapp.db.mealDatabase

class HomeViewModelFactory (private val mealDatabase: mealDatabase): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return homeViewModel(mealDatabase) as T
    }
}