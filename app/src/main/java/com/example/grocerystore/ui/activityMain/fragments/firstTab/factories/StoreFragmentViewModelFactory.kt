package com.example.grocerystore.ui.activityMain.fragments.firstTab.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.grocerystore.data.repository.categories.CategoriesRepoInterface
import com.example.grocerystore.data.repository.dishes.DishesRepoInterface
import com.example.grocerystore.data.repository.user.UserRepoInterface
import com.example.grocerystore.services.CreatingNewIdsService
import com.example.grocerystore.ui.activityMain.fragments.firstTab.viewModels.StoreFragmentViewModel

class StoreFragmentViewModelFactory(private val dishesRepository: DishesRepoInterface, private val categoriesRepository: CategoriesRepoInterface, private val userRepository : UserRepoInterface, private val  creatingNewIdsService: CreatingNewIdsService ) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoreFragmentViewModel::class.java)) {
            return StoreFragmentViewModel(dishesRepository, userRepository, creatingNewIdsService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}