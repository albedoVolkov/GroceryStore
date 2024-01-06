package com.example.grocerystore.ui.activityMain.fragments.firstTab.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerystore.data.helpers.UIstates.item.DishUIState
import com.example.grocerystore.data.repository.user.UserRepository
import com.example.grocerystore.locateLazy
import com.example.grocerystore.services.FactoryService
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class InfoDishFragmentViewModel() : ViewModel() {

    private val TAG = "InfoDishFragmentViewModel"


    private val userRepository by locateLazy<UserRepository>()
    private val factory by locateLazy<FactoryService>()

    //ITEMS
    var mainDish : DishUIState? = null

    fun addMainDishInBasketOfUser() : Boolean{
        var resultMain = false
        viewModelScope.launch {

                val userResult =  async { userRepository.getCurrentUser()}.await() //error
                Log.d(TAG, "addMainDishInBasketOfUser - user - ${userResult.getOrNull()}")
                if (userResult.isSuccess && userResult.getOrNull() != null) {

                    val cartResult = factory.createCartUIState(userResult.getOrNull()!!.userId, mainDish!!,1)
                    Log.d(TAG, "addMainDishInBasketOfUser - cartResult - ${cartResult.getOrNull()}")
                    if (cartResult.isSuccess && cartResult.getOrNull() != null) {

                        val result = async { userRepository.addCartInBasketOfCurrentUser(cartResult.getOrNull()!!)}.await()
                        Log.d(TAG, "addMainDishInBasketOfUser - result - ${result.getOrNull()}")
                        if (result.isSuccess && result.getOrNull() != null) { resultMain = true }

                    }else{ Log.d(TAG, "addMainDishInBasketOfUser - newId - ERROR") }
                } else { Log.d(TAG, "addMainDishInBasketOfUser - user - ERROR") }
        }
        return resultMain
    }




}