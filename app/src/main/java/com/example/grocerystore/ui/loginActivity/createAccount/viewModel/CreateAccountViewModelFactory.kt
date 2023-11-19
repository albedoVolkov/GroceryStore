package com.example.grocerystore.ui.loginActivity.createAccount.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.grocerystore.data.repository.user.UserRepoInterface
import com.example.grocerystore.services.CreatingNewIdsService

class CreateAccountViewModelFactory(private val repository : UserRepoInterface, private val creatingIdsService: CreatingNewIdsService) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateAccountViewModel::class.java)) {
            return CreateAccountViewModel(repository,creatingIdsService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}