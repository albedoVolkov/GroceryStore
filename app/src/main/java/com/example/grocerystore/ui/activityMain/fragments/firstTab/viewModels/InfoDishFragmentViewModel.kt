package com.example.grocerystore.ui.activityMain.fragments.firstTab.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerystore.data.helpers.UIstates.item.DishUIState
import com.example.grocerystore.data.helpers.UIstates.user.UserUIStateShort
import com.example.grocerystore.data.repository.dishes.DishesRepoInterface
import com.example.grocerystore.data.repository.user.UserRepoInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class InfoDishFragmentViewModel(
    private val dishesRepository: DishesRepoInterface,
    private val userRepository: UserRepoInterface,) : ViewModel() {

    companion object {
        const val TAG = "InfoDishFragmentViewModel"
    }

    //PRODUCT
    private val _mainDish = MutableLiveData<DishUIState?>()
    val mainDish: LiveData<DishUIState?> get() = _mainDish

    private var _mainDishId: Int = -1


    //USER
    private val _userData = MutableLiveData<UserUIStateShort?>()
    val userData: LiveData<UserUIStateShort?> get() = _userData


    fun refreshInfo() {
        if(_mainDishId != -1) {
            Log.d(TAG, "refreshInfo: _mainDishId = $_mainDishId")
            viewModelScope.launch {
                val dataRefresh = async { dishesRepository.observeDishItemById(_mainDishId) }
                val item = dataRefresh.await()
                if (dataRefresh.await() != null) {
                    Log.d(TAG, "refreshInfo: data : Success = $item")
                    _mainDish.value = item
                } else {
                    Log.d(TAG, "refreshDishes: data : Error = null")
                    _mainDish.value = null
                }
            }
        }else{
            Log.d(TAG, "refreshInfo: _mainDishId = -1")
        }
    }

    fun setMainDishId(dishId: Int) {
        Log.d(TAG, "setMainDishId is $dishId")
        _mainDishId = dishId
        dishesRepository.setDishListId(dishId)
    }
}