package com.example.grocerystore.ui.activityMain.fragments.forthTab.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState
import com.example.grocerystore.data.repository.user.UserRepoInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AccountFragmentViewModel(private val userRepository : UserRepoInterface) : ViewModel(){


    companion object{
        private const val TAG = "AccountFragmentViewModel"
    }

    //user
    private val _userData = MutableLiveData<UserUIState?>()
    val userData: LiveData<UserUIState?> get() = _userData


    init{
        getCurrentUserData()
    }


    private fun getCurrentUserData(){
        viewModelScope.launch {
            val currentUser = async { userRepository.getCurrentUser()}.await().getOrNull()
            if (currentUser != null) {
                Log.d(TAG, " getCurrentUserData : SUCCESS : listData is $currentUser")
                _userData.value = currentUser
            } else {
                _userData.value = null
                Log.d(TAG, "getCurrentUserData : ERROR : listData is null")
            }
        }
    }

}