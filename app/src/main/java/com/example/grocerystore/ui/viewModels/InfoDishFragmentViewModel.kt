package com.example.grocerystore.ui.viewModels

import android.service.autofill.UserData
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerystore.GroceryStoreApplication
import com.example.grocerystore.data.helpers.CategoryUIState
import com.example.grocerystore.data.helpers.DishUIState
import com.example.grocerystore.data.helpers.TitleUIState
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class InfoDishFragmentViewModel(application: GroceryStoreApplication) : ViewModel() {

    private val TAG = "InfoDishFragmentViewModel"

    private val dishesRepository = application.dishesRepository

    private val _mainDish = MutableLiveData<DishUIState?>()
    val mainDish: LiveData<DishUIState?> get() = _mainDish

    private var _mainDishId : Int = -1

    private val _userData = MutableLiveData<UserData?>()
    val userData: LiveData<UserData?> get() = _userData


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