package com.example.grocerystore.ui.activityMain.fragments.firstTab.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerystore.data.helpers.UIstates.item.CategoryUIState
import com.example.grocerystore.data.helpers.UIstates.item.DishUIState
import com.example.grocerystore.data.helpers.UIstates.item.TitleUIState
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState
import com.example.grocerystore.data.repository.categories.CategoriesRepoInterface
import com.example.grocerystore.data.repository.dishes.DishesRepoInterface
import com.example.grocerystore.data.repository.user.UserRepoInterface
import com.example.grocerystore.services.CreatingNewIdsService

import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private const val TAG = "StoreFragmentViewModel"

class StoreFragmentViewModel(private val dishesRepository: DishesRepoInterface, private val userRepository : UserRepoInterface, private val creatingIdsService : CreatingNewIdsService) : ViewModel() {

    private var mainCategory : CategoryUIState? = null

    private var _showDishes = MutableLiveData<List<DishUIState>>()
    val showDishes: LiveData<List<DishUIState>> get() = _showDishes

    private var _mainDishes = MutableLiveData<List<DishUIState>>()



    private var _showTitles = MutableLiveData<List<TitleUIState>>()
    val showTitles: LiveData<List<TitleUIState>> get() = _showTitles

    private var _mainTitles = MutableLiveData<List<TitleUIState>>()




    private var _filterType = MutableLiveData("All")
    val filterType: LiveData<String> get() = _filterType


    //USER
    private val _userData = MutableLiveData<UserUIState?>()
    val userData: LiveData<UserUIState?> get() = _userData



    init{
        getCurrentUserData()
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



    fun filterDishes(filterType: String) {
        Log.d(TAG, "filterType is $filterType")
        _showDishes.value = when (filterType) {
            "None" -> emptyList()
            "All" -> _mainDishes.value ?: emptyList()
            else -> {
                val lengthComparator =
                    Comparator { i1: DishUIState, i2: DishUIState -> i1.id.toInt() - i2.id.toInt()  }
                _mainDishes.value?.sortedWith(lengthComparator)
            }
        }
    }

    fun setMainCategory(category : CategoryUIState) {
        Log.d(TAG, "setMainCategory is $category")
        mainCategory = category

        viewModelScope.launch {
            dishesRepository.setDishListId(category.id)
        }
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
        var listTitlesNames = mutableListOf<String>()
        val listTitles = mutableListOf<TitleUIState>()

        viewModelScope.launch {


            if (list != null) {

                for (elem in list) {
                    listTitlesNames.addAll(elem.tags)
                }

                listTitlesNames = listTitlesNames.toSet().toMutableList()//sorted names of tags

                for (elem in listTitlesNames) {
                    val idElement = async {  creatingIdsService.createIdForTitleUIState()}.await()
                    if (idElement.isSuccess) {
                        listTitles.add(TitleUIState(idElement.getOrDefault("-1")!!, elem))
                    }
                }
                _showTitles.value = listTitles

                Log.d(TAG, "refreshTitles: data : Success = ${_showTitles.value}")
            } else {
                Log.d(TAG, "refreshTitles: data : Error = null")
            }
        }
    }

    fun getDishById(dishId: String): DishUIState? {
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