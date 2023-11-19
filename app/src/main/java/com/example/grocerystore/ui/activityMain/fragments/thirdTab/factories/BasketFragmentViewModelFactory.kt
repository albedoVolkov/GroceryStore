package com.example.grocerystore.ui.activityMain.fragments.thirdTab.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.grocerystore.data.repository.user.UserRepoInterface
import com.example.grocerystore.services.ShoppingAppSessionManager
import com.example.grocerystore.ui.activityMain.fragments.thirdTab.viewModels.BasketFragmentViewModel

class BasketFragmentViewModelFactory(private val repository : UserRepoInterface, private val sessionManager : ShoppingAppSessionManager) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BasketFragmentViewModel::class.java)) {
            return BasketFragmentViewModel(repository,sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}