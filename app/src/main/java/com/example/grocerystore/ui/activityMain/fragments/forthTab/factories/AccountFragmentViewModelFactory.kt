package com.example.grocerystore.ui.activityMain.fragments.forthTab.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.grocerystore.data.repository.user.UserRepoInterface
import com.example.grocerystore.ui.activityMain.fragments.forthTab.viewModels.AccountFragmentViewModel

class AccountFragmentViewModelFactory(private val repository : UserRepoInterface) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountFragmentViewModel::class.java)) {
            return AccountFragmentViewModel(repository,) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}