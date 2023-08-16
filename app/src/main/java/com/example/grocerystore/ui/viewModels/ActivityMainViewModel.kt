package com.example.grocerystore.ui.viewModels

import android.service.autofill.UserData
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.grocerystore.GroceryStoreApplication

class ActivityMainViewModel(application: GroceryStoreApplication) : ViewModel()  {

//    private val authRepository =
//        (application.applicationContext as GroceryStoreApplication).authRepository

    //private val sessionManager = GroceryStoreSessionManager(application.applicationContext)
    //private val currentUser = sessionManager.getUserIdFromSession()
    //val isUserASeller = sessionManager.isUserSeller()

    private val _userData = MutableLiveData<UserData?>()
    val userData: LiveData<UserData?> get() = _userData
}