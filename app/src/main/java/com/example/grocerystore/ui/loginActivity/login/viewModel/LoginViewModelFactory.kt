package com.example.grocerystore.ui.loginActivity.login.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.grocerystore.data.repository.user.UserRepoInterface
import com.example.grocerystore.services.ShoppingAppSessionManager

class LoginViewModelFactory(private val repository : UserRepoInterface,private val  sessionManager: ShoppingAppSessionManager) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(repository,sessionManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}