package com.example.grocerystore.ui.activityMain.fragments.firstTab.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.grocerystore.data.repository.user.UserRepoInterface
import com.example.grocerystore.services.CreatingNewIdsService
import com.example.grocerystore.ui.activityMain.fragments.firstTab.viewModels.InfoDishFragmentViewModel

class InfoDishFragmentViewModelFactory (private val userRepository: UserRepoInterface,private val creatingNewIdsService: CreatingNewIdsService) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InfoDishFragmentViewModel::class.java)) {
            return InfoDishFragmentViewModel(userRepository,creatingNewIdsService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
