package com.example.grocerystore.ui.activityMain.mainViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.grocerystore.data.repository.user.UserRepoInterface

class ActivityMainViewModelFactory(private val userRepository : UserRepoInterface) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActivityMainViewModel::class.java)) {
            return ActivityMainViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}