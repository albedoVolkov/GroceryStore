package com.example.grocerystore.ui.activityMain.fragments.forthTab.viewModels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerystore.data.repository.user.UserRepository
import com.example.grocerystore.locateLazy
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn

class AccountFragmentViewModel() : ViewModel(){

    private val TAG = "AccountFragmentViewModel"

    private val userRepository by locateLazy<UserRepository>()

        val userData = userRepository.getCurrentUserFlow().shareIn(viewModelScope,SharingStarted.Lazily, replay = 1)
}