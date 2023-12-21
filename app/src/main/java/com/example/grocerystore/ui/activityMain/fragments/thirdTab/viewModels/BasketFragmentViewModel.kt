package com.example.grocerystore.ui.activityMain.fragments.thirdTab.viewModels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerystore.data.helpers.UIstates.item.CartUIState
import com.example.grocerystore.data.repository.cart.CartRepository
import com.example.grocerystore.data.repository.user.UserRepository
import com.example.grocerystore.locateLazy
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch

class BasketFragmentViewModel() : ViewModel(){

    private val TAG = "BasketFragmentViewModel"

    private val userRepository by locateLazy<UserRepository>()
    private val cartsRepository by locateLazy<CartRepository>()

    //USER
    val userData = userRepository.getCurrentUserFlow().asLiveDataFlow()

    //CARTS
    val mainCarts = cartsRepository.getCurrentUserCartsFlow().asLiveDataFlow()

    private var _showCarts : Flow<List<CartUIState>> = flow { emptyList<CartUIState>() }
    val showCarts: Flow<List<CartUIState>> get() = _showCarts.asLiveDataFlow()

    private var _filterType : String = "All"
    val filterType: String get() = _filterType
    

    private fun <T> Flow<T>.asLiveDataFlow() = shareIn(viewModelScope, SharingStarted.Eagerly, replay = 1)




    fun filterItems(filterType: String) {
        _showCarts = when (filterType) {
            "None" -> flow { emptyList<CartUIState>() }
            "All" -> mainCarts
            "Reversed" -> flow { mainCarts.onEach { list -> list.reversed() } }
            else -> mainCarts
        }
    }



    fun increaseQuantityOfCart(cart : CartUIState) : Boolean{
        var result = false
        viewModelScope.launch {

            val dataUpdateCart = async {
                cartsRepository.updateCartInBasketOfCurrentUser(cart.copy(quantity  = cart.quantity + 1))
            }.await()

            result = dataUpdateCart.isSuccess && dataUpdateCart.getOrNull() == true
        }

        return result
    }



    fun decreaseQuantityOfCart(cart : CartUIState) : Boolean{
        var result = false
        if(cart.quantity > 1 ) {
            viewModelScope.launch {

                val dataUpdateCart = async {
                    cartsRepository.updateCartInBasketOfCurrentUser(cart.copy(quantity = cart.quantity - 1))
                }.await()

                result = dataUpdateCart.isSuccess && dataUpdateCart.getOrNull() == true
            }
        }
        return result
    }



    fun getPriceAllItems(list : List<CartUIState>) : Int{
        var priceAllElements = 0
        for(elem in list){
            val elemPrice  = elem.quantity * elem.itemData.price
            priceAllElements += elemPrice
        }
        return priceAllElements
    }

}