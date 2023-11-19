package com.example.grocerystore.ui.activityMain.fragments.firstTab.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerystore.GroceryStoreApplication
import com.example.grocerystore.data.helpers.UIstates.item.CartUIState
import com.example.grocerystore.data.helpers.UIstates.item.DishUIState
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState
import com.example.grocerystore.data.helpers.UIstates.user.UserUIStateShort
import com.example.grocerystore.data.repository.dishes.DishesRepoInterface
import com.example.grocerystore.data.repository.user.UserRepoInterface
import com.example.grocerystore.services.CreatingNewIdsService
import com.example.grocerystore.ui.activityMain.fragments.thirdTab.viewModels.BasketFragmentViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class InfoDishFragmentViewModel(
    private val userRepository: UserRepoInterface,private val creatingNewIdsService: CreatingNewIdsService
) : ViewModel() {

    companion object {
        const val TAG = "InfoDishFragmentViewModel"
    }

    init{
        getCurrentUserData()
    }


    //USER
    private val _userData = MutableLiveData<UserUIState?>()
    val userData: LiveData<UserUIState?> get() = _userData


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

    fun addProductInBasketOfUser(dishUIState: DishUIState) {
        viewModelScope.launch {
            val newId = creatingNewIdsService.createIdForCartUIState().getOrNull()
            if (newId != null) {

                val user = userData.value
                if (user != null) {

                    val cart = CartUIState(newId, user.userId, dishUIState,)
                    Log.d(TAG, "addProductInBasketOfUser is $cart - SUCCESS")
                    userRepository.addProductInBasketOfUser(cart, user.userId)

                } else {
                    Log.d(TAG, "addProductInBasketOfUser is null - ERROR")
                }

            }
        }
    }
}