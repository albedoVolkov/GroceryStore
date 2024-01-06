package com.example.grocerystore.ui.activityMain.fragments.firstTab.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerystore.data.helpers.UIstates.item.CategoryUIState
import com.example.grocerystore.data.helpers.UIstates.item.DishUIState
import com.example.grocerystore.data.helpers.UIstates.item.TitleUIState
import com.example.grocerystore.data.repository.dishes.DishesRepository
import com.example.grocerystore.data.repository.user.UserRepository
import com.example.grocerystore.locateLazy
import com.example.grocerystore.services.FactoryService
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch



class StoreFragmentViewModel() : ViewModel() {


    private val TAG = "StoreFragmentViewModel"


    private val dishesRepository by locateLazy<DishesRepository>()
    private val userRepository by locateLazy<UserRepository>()
    private val factory by locateLazy<FactoryService>()


    //USER
    val userData = userRepository.getCurrentUserFlow().asLiveDataFlow()



    //ITEMS
    private var mainCategory : CategoryUIState? = null



    private var _showDishes : List<DishUIState> = listOf()
    val showDishes: List<DishUIState> get() = _showDishes

    val mainDishes = dishesRepository.getDishesListFlow().asLiveDataFlow()// this list isn't for showing and not sorted



    private var _showTitles : List<TitleUIState> = listOf()
    val showTitles: List<TitleUIState> get() = _showTitles

    var mainTitles: Flow<List<TitleUIState>> = flow{ listOf<TitleUIState>()}


    private fun <T> Flow<T>.asLiveDataFlow() = shareIn(viewModelScope, SharingStarted.Eagerly, replay = 1)




    fun filterDishes(filterType: String,list : List<DishUIState>) {
        _showDishes = when (filterType) {
            "None" -> emptyList()
            "All" -> list
            "Reversed" -> list.reversed()
            else -> list
        }
    }



    fun filterTitles(filterType: String,list : List<TitleUIState>) {
        _showTitles = when (filterType) {
            "None" -> emptyList()
            "All" -> list
            "Reversed" -> list.reversed()
            else -> list
        }
    }



    fun setMainCategory(category : CategoryUIState) {
        Log.d(TAG, "setMainCategory is $category")
        mainCategory = category
    }



    fun refreshDishes(category : CategoryUIState) : Boolean {
        Log.d(TAG, "refreshDishes : category - $category")

        var res = false
        viewModelScope.launch {

           val resultRefresh =  async {  dishesRepository.refreshDishesData(category.id)}.await()

            Log.d(TAG, "refreshDishes : resultRefresh - ${resultRefresh.getOrNull()}")

            if(resultRefresh.isSuccess && resultRefresh.getOrNull() == true){ res = true }
        }
        return res
    }



    fun refreshTitles(list : List<DishUIState>): Boolean{

        var res = false
        Log.d(TAG, "refreshTitles : list - $list")

        var listTitlesNames = mutableListOf<String>()
        val listTitles = mutableListOf<TitleUIState>()

        viewModelScope.launch {

            for (elem in list) {
                listTitlesNames.addAll(elem.tags)
            }

            listTitlesNames = listTitlesNames.toSet().toMutableList()//sorted names of tags
            Log.d(TAG, "refreshTitles : listTitlesNames - $listTitlesNames")

            for (name in listTitlesNames) {
                val item = async {  factory.createTitleUIState(name)}.await()
                if (item.isSuccess && item.getOrNull() != null) {
                    listTitles.add(item.getOrNull()!!)
                }
            }
            mainTitles = flow{emit(listTitles)}
            Log.d(TAG, "refreshTitles : mainTitles - $listTitles")
            res = true
        }
        return res
    }

}