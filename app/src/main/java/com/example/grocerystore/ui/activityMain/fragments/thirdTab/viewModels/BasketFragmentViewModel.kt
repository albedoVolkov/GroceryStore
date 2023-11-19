package com.example.grocerystore.ui.activityMain.fragments.thirdTab.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerystore.data.helpers.UIstates.item.CartUIState
import com.example.grocerystore.data.helpers.UIstates.item.CategoryUIState
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState
import com.example.grocerystore.data.helpers.UIstates.user.UserUIStateShort
import com.example.grocerystore.data.repository.categories.CategoriesRepoInterface
import com.example.grocerystore.data.repository.user.UserRepoInterface
import com.example.grocerystore.services.ShoppingAppSessionManager
import com.example.grocerystore.ui.activityMain.fragments.firstTab.viewModels.CategoriesFragmentViewModel
import com.example.grocerystore.ui.activityMain.fragments.firstTab.viewModels.InfoDishFragmentViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class BasketFragmentViewModel(private val userRepository : UserRepoInterface, sessionManager : ShoppingAppSessionManager) : ViewModel(){


        companion object{
            private const val TAG = "BasketFragmentViewModel"
        }

        //user
        private val _userData = MutableLiveData<UserUIState?>()
        val userData: LiveData<UserUIState?> get() = _userData

        //carts
        private var _mainCarts : MutableLiveData<List<CartUIState>> = MutableLiveData<List<CartUIState>>()// this list isn't for showing and not sorted
        val mainCarts: LiveData<List<CartUIState>> get() = _mainCarts

        private var _showCarts : MutableLiveData<List<CartUIState>> = MutableLiveData<List<CartUIState>>()
        val showCarts: LiveData<List<CartUIState>> get() = _showCarts

        private var _filterType = MutableLiveData("All")
        val filterType: LiveData<String> get() = _filterType


        init{
            getCurrentUserData()

            getCartItems()
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


        fun filterItems(filterType: String) {
            Log.d(TAG, "filterType is $filterType and list is ${_mainCarts.value}")
            _showCarts.value = when (filterType) {
                "None" -> emptyList()
                "All" -> _mainCarts.value ?: emptyList()
                "Reversed" -> _mainCarts.value!!.toMutableList().reversed() ?: emptyList()
                else -> {
                    val lengthComparator =
                        Comparator { i1: CartUIState, i2: CartUIState -> (i1.cartId).toInt() - (i2.cartId).toInt()  }
                    _mainCarts.value?.sortedWith(lengthComparator)
                }
            }
        }


        private fun getCartItems() {
            viewModelScope.launch {

                val currentUser = async { userRepository.getCurrentUser()}.await().getOrNull()

                if(currentUser != null) {
                    Log.d(TAG, "getCartItems: data : Done = ${currentUser.cart}")
                    _mainCarts.value = currentUser.cart
                }else{
                    Log.d(TAG, "getCartItems: data : Error = null")
                    _mainCarts.value = emptyList()
                }
                _showCarts.value = _mainCarts.value
            }
        }


    private fun increaseQuantityOfCart(cart : CartUIState) : Boolean{
        var result = false
        viewModelScope.launch {
            async {  getCurrentUserData()}.await()

            if(userData.value != null) {

                val dataUpdateCart = async {
                    userRepository.updateProductInBasketOfUser(
                        cart.copy(quantity  = cart.quantity + 1)
                        ,userData.value?.userId!!
                    )
                }.await()

                if(dataUpdateCart.isFailure){ Log.d(TAG, "increaseQuantityOfCart: dataUpdateCart : Error")

                }else{ result = true }
            }else{ Log.d(TAG, "increaseQuantityOfCart: userData : Error") }
        }
        return result
    }


    private fun decreaseQuantityOfCart(cart : CartUIState) : Boolean{
        var result = false
        viewModelScope.launch {
            async {  getCurrentUserData()}.await()

            if(userData.value != null) {

                val dataUpdateCart = async {
                    userRepository.updateProductInBasketOfUser(
                        cart.copy(quantity = cart.quantity - 1)
                        ,userData.value?.userId!!
                    )
                }.await()

                if(dataUpdateCart.isFailure){
                    Log.d(TAG, "decreaseQuantityOfCart: dataUpdateCart : Error")

                }else{ result = true }
            }else{ Log.d(TAG, "decreaseQuantityOfCart: userData : Error") }
        }
        return result
    }


    private fun getPriceAllItems(list : List<CartUIState>) : Int{
        var priceAllElements = 0
        for(elem in list){
            val elemPrice  = elem.quantity * elem.itemData.price
            priceAllElements += elemPrice
        }
        return priceAllElements
    }

}