package com.example.grocerystore.ui.activityMain.fragments.firstTab.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerystore.data.helpers.UIstates.item.CartUIState
import com.example.grocerystore.data.helpers.UIstates.item.DishUIState
import com.example.grocerystore.data.repository.cart.CartRepository
import com.example.grocerystore.data.repository.user.UserRepository
import com.example.grocerystore.locateLazy
import com.example.grocerystore.services.IdService
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class InfoDishFragmentViewModel() : ViewModel() {

    private val TAG = "InfoDishFragmentViewModel"


    private val cartRepository by locateLazy<CartRepository>()
    private val userRepository by locateLazy<UserRepository>()
    private val idService by locateLazy<IdService>()

    //ITEMS
    var mainDish : DishUIState? = null

    fun addMainDishInBasketOfUser() {
        viewModelScope.launch {

            val newId = async { idService.createIdForCartUIState().getOrNull()}.await()
            if (newId != null) {

                val user = userRepository.getCurrentUser()
                if (user.isSuccess && user.getOrNull() != null) {

                    val cart = CartUIState(newId, user.getOrNull()!!.userId, mainDish!!,)
                    Log.d(TAG, "addProductInBasketOfUser is $cart - SUCCESS")
                    async { cartRepository.addCartInBasketOfCurrentUser(cart)}.await()

                } else {
                    Log.d(TAG, "addProductInBasketOfUser is null - ERROR")
                }

            }
        }
    }




}