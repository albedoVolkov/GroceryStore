package com.example.grocerystore.activityMain.ui.firstTab.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.grocerystore.GroceryStoreApplication
import com.example.grocerystore.ShoppingAppSessionManager
import com.example.grocerystore.data.repository.categories.CategoriesRepoInterface
import com.example.grocerystore.data.repository.user.UserRepoInterface
import com.example.grocerystore.activityMain.ui.firstTab.viewModels.CategoriesFragmentViewModel

class CategoriesFragmentViewModelFactory(private val repository : CategoriesRepoInterface,private val sessionManager : ShoppingAppSessionManager) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CategoriesFragmentViewModel::class.java)) {
            return CategoriesFragmentViewModel(repository,sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}