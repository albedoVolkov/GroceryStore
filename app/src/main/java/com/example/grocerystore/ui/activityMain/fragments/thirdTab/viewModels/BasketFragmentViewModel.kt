package com.example.grocerystore.ui.activityMain.fragments.thirdTab.viewModels


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerystore.data.helpers.UIstates.item.CartUIState
import com.example.grocerystore.data.repository.user.UserRepository
import com.example.grocerystore.locateLazy
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

class BasketFragmentViewModel() : ViewModel(){

    private val TAG = "BasketFragmentViewModel"

    private val userRepository by locateLazy<UserRepository>()

    //USER
    val userData = userRepository.getCurrentUserFlow().asLiveDataFlow()

    private var _showCarts : List<CartUIState> =  emptyList<CartUIState>()
    val showCarts: List<CartUIState> get() = _showCarts

    private fun <T> Flow<T>.asLiveDataFlow() = shareIn(viewModelScope, SharingStarted.Eagerly, replay = 1)




    fun filterItems(filterType: String, list : List<CartUIState>) {
        _showCarts = when (filterType) {
            "None" -> emptyList()
            "All" -> list
            "Reversed" -> list.reversed()
            else -> list
        }
    }



    fun increaseQuantityOfCart(cart : CartUIState) : Boolean{
        var result = false
        viewModelScope.launch {

            val dataUpdateCart = async {
                userRepository.updateCartInBasketOfCurrentUser(cart.copy(quantity = cart.quantity + 1))
            }.await()
            Log.d(TAG, " increaseQuantityOfCart : dataUpdateCart - $dataUpdateCart")
            if( dataUpdateCart.isSuccess && dataUpdateCart.getOrNull() != null){
                result = true
            }
        }

        return result
    }



    fun decreaseQuantityOfCart(cart : CartUIState) : Boolean{
        var result = false
        if(cart.quantity > 1 ) {
            viewModelScope.launch {

                val dataUpdateCart = async {
                    userRepository.updateCartInBasketOfCurrentUser(cart.copy(quantity = cart.quantity - 1))
                }.await()
                Log.d(TAG, " decreaseQuantityOfCart : dataUpdateCart - $dataUpdateCart")
                if( dataUpdateCart.isSuccess && dataUpdateCart.getOrNull() != null){
                    result = true
                }
            }
        }
        return result
    }

    fun deleteCart(cartId : String) : Boolean{
        var result = false
            viewModelScope.launch {

                val deleteCartResult = async {
                    userRepository.deleteCartInBasketOfCurrentUser(cartId)
                }.await()

                Log.d(TAG, " deleteCart : result - $deleteCartResult")
                if( deleteCartResult.isSuccess && deleteCartResult.getOrNull() != null){
                    result = true
                }
            }
        return result
    }




    fun getPriceAllItems(list : List<CartUIState>):Int{
        Log.d(TAG, " getPriceAllItems : list - $list")
        var priceAll = 0
        for(elem in list){
            val elemPrice  = elem.quantity * elem.itemData.price
            priceAll += elemPrice
        }
        return priceAll
    }

}