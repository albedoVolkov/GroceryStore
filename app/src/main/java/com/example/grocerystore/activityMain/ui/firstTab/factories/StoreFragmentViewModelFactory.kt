package com.example.grocerystore.activityMain.ui.firstTab.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.grocerystore.GroceryStoreApplication
import com.example.grocerystore.ShoppingAppSessionManager
import com.example.grocerystore.data.repository.categories.CategoriesRepoInterface
import com.example.grocerystore.data.repository.dishes.DishesRepoInterface
import com.example.grocerystore.data.repository.dishes.DishesRepository
import com.example.grocerystore.activityMain.ui.firstTab.viewModels.InfoDishFragmentViewModel
import com.example.grocerystore.activityMain.ui.firstTab.viewModels.StoreFragmentViewModel

class StoreFragmentViewModelFactory(private val dishesRepository: DishesRepoInterface, private val categoriesRepository: CategoriesRepoInterface,private val sessionManager : ShoppingAppSessionManager) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoreFragmentViewModel::class.java)) {
            return StoreFragmentViewModel(dishesRepository,categoriesRepository,sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}