package com.example.grocerystore.ui.viewModels

import android.service.autofill.UserData
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerystore.GroceryStoreApplication
import com.example.grocerystore.data.helpers.DishUIState
import com.example.grocerystore.data.helpers.TitleUIState
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private const val TAG = "StoreFragmentViewModel"

class StoreFragmentViewModel(application: GroceryStoreApplication) : ViewModel() {

    private val dishesRepository = application.dishesRepository

    var showNameCategory: String? = null

    private var _showDishes = MutableLiveData<List<DishUIState>>()
    val showDishes: LiveData<List<DishUIState>> get() = _showDishes

    private var _mainDishes = MutableLiveData<List<DishUIState>>()
    val mainDishes: LiveData<List<DishUIState>> get() = _mainDishes

    private var _showTitles = MutableLiveData<List<TitleUIState>>()
    val showTitles: LiveData<List<TitleUIState>> get() = _showTitles

    private var _filterType = MutableLiveData("All")
    val filterType: LiveData<String> get() = _filterType

    private var _dataSourceCategory : Int = -1
    val dataSourceCategory: Int get() = _dataSourceCategory

    private val _userData = MutableLiveData<UserData?>()
    val userData: LiveData<UserData?> get() = _userData


    fun filterDishes(filterType: String) {
        Log.d(TAG, "filterType is $filterType")
        _showDishes.value = when (filterType) {
            "None" -> emptyList()
            "All" -> _mainDishes.value ?: emptyList()
            else -> {
                val lengthComparator =
                    Comparator { i1: DishUIState, i2: DishUIState -> i1.id - i2.id }
                _mainDishes.value?.sortedWith(lengthComparator)
            }
        }
    }

    fun setCategoryId(id: Int) {
        Log.d(TAG, "setCategoryId is $id")
        _dataSourceCategory = id
        dishesRepository.setDishListId(_dataSourceCategory)
    }

    fun refreshDishes() {
        viewModelScope.launch {

            val dataRefresh = async {  dishesRepository.refreshDishesData()}

            if(dataRefresh.await()) {
                val res = async { dishesRepository.observeListDishes() }
                val list = res.await()
                Log.d(TAG, "refreshDishes: data : Success = $list")
                _mainDishes.value = list
                refreshTitles(list)
            }else{
                Log.d(TAG, "refreshDishes: data : Error = null")
                _mainDishes.value = emptyList()
            }
            _showDishes.value = _mainDishes.value
        }
    }

    private fun refreshTitles(list : List<DishUIState>?){
        var countId = 0
        var listTitlesNames = mutableListOf<String>()
        val listTitles = mutableListOf<TitleUIState>()
        if(list != null){

            for(elem in list){
                listTitlesNames.addAll(elem.tags)
            }

            listTitlesNames = listTitlesNames.toSet().toMutableList()//sorted names of tags

            for(elem in listTitlesNames){
                listTitles.add(TitleUIState(countId.toLong(),elem))
                countId++
            }
            _showTitles.value = listTitles

            Log.d(TAG, "refreshTitles: data : Success = ${_showTitles.value}")
        }else{
            Log.d(TAG, "refreshTitles: data : Error = null")
        }
    }

    fun getDishById(dishId: Int): DishUIState? {
        var data : DishUIState? = null
        viewModelScope.launch {
            val dataRefresh = async { dishesRepository.observeDishItemById(dishId)}
            data = if(dataRefresh.await() != null) {
                Log.d(TAG, "getDishById: data : Success = ${dataRefresh.await()}")
                dataRefresh.await()
            }else{
                Log.d(TAG, "getDishById: data : Error = null")
                null
            }
        }
        return data
    }
}