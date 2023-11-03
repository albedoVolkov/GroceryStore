package com.example.grocerystore.activityMain.mainViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.grocerystore.GroceryStoreApplication

class ActivityMainViewModelFactory(private val application: GroceryStoreApplication) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActivityMainViewModel::class.java)) {
            return ActivityMainViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}