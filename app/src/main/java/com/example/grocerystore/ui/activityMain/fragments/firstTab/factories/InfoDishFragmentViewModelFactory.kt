package com.example.grocerystore.ui.activityMain.fragments.firstTab.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.grocerystore.data.repository.dishes.DishesRepoInterface
import com.example.grocerystore.data.repository.user.UserRepoInterface
import com.example.grocerystore.ui.activityMain.fragments.firstTab.viewModels.InfoDishFragmentViewModel

class InfoDishFragmentViewModelFactory (private val dishesRepository: DishesRepoInterface,private val userRepository: UserRepoInterface) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InfoDishFragmentViewModel::class.java)) {
            return InfoDishFragmentViewModel(dishesRepository,userRepository,) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}