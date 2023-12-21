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
import com.example.grocerystore.services.IdService
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch



class StoreFragmentViewModel() : ViewModel() {


    private val TAG = "StoreFragmentViewModel"


    private val dishesRepository by locateLazy<DishesRepository>()
    private val userRepository by locateLazy<UserRepository>()
    private val idService by locateLazy<IdService>()

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




    suspend fun filterDishes(filterType: String) {
        _showDishes = when (filterType) {
            "None" -> emptyList()
            "All" -> mainDishes.last()
            "Reversed" -> mainDishes.last().reversed()
            else -> mainDishes.last()
        }
    }

    suspend fun filterTitles(filterType: String) {
        _showTitles = when (filterType) {
            "None" -> emptyList()
            "All" -> mainTitles.last()
            "Reversed" -> mainTitles.last().reversed()
            else -> mainTitles.last()
        }
    }

    fun setMainCategory(category : CategoryUIState) {
        Log.d(TAG, "setMainCategory is $category")
        mainCategory = category
    }


    fun refreshDishes(category : CategoryUIState) {
        viewModelScope.launch {
            async {  dishesRepository.refreshDishesData(category.id)}.await()
            refreshTitles(mainDishes.last())
        }
    }


    private fun refreshTitles(list : List<DishUIState>){
        var listTitlesNames = mutableListOf<String>()
        val listTitles = mutableListOf<TitleUIState>()

        viewModelScope.launch {

            for (elem in list) {
                listTitlesNames.addAll(elem.tags)
            }

            listTitlesNames = listTitlesNames.toSet().toMutableList()//sorted names of tags

            for (elem in listTitlesNames) {
                val idElement = async {  idService.createIdForTitleUIState()}.await()
                if (idElement.isSuccess) {
                    listTitles.add(TitleUIState(idElement.getOrDefault("-1")!!, elem))
                }
            }
            mainTitles = flow{emit(listTitles)}
        }
    }

}