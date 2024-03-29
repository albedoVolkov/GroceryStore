package com.example.grocerystore.ui.activityMain.fragments.firstTab.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerystore.domain.models.item.CategoryUIState
import com.example.grocerystore.domain.models.item.DishUIState
import com.example.grocerystore.domain.models.item.TitleUIState
import com.example.grocerystore.data.repository.dishes.DishesRepository
import com.example.grocerystore.data.repository.user.UserRepository
import com.example.grocerystore.locateLazy
import com.example.grocerystore.domain.services.FactoryService
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch



class StoreFragmentViewModel : ViewModel() {

    companion object{
       const val TAG = "StoreFragmentViewModel"
    }

    private val dishesRepository by locateLazy<DishesRepository>()
    private val userRepository by locateLazy<UserRepository>()
    private val factory by locateLazy<FactoryService>()


    //USER
    val userData = userRepository.getCurrentUserFlow().asLiveDataFlow()

    //ITEMS
    private var _mainCategory : CategoryUIState? = null
    val mainCategory : CategoryUIState? get() = _mainCategory


    private var _showDishes : List<DishUIState> = listOf()
    val showDishes: List<DishUIState> get() = _showDishes

    private var _mainDishes : List<DishUIState> = listOf()
    val mainDishes: List<DishUIState> get() = _mainDishes// this list isn't for showing and not sorted

    val providerDishes = dishesRepository.getDishesListFlow().asLiveDataFlow()




    private var _showTitles : List<TitleUIState> = listOf()
    val showTitles: List<TitleUIState> get() = _showTitles

    private var _mainTitles : List<TitleUIState> = listOf()
    val mainTitles: List<TitleUIState> get() = _mainTitles// this list isn't for showing and not sorted

    var providerTitles: MutableStateFlow<List<TitleUIState>> = MutableStateFlow(listOf())


    private fun <T> Flow<T>.asLiveDataFlow() = shareIn(viewModelScope, SharingStarted.Eagerly, replay = 1)




    fun filterDishes(filterType: String) {
        _showDishes = when (filterType) {
            "None" -> emptyList()
            "All" -> _mainDishes
            "Reversed" -> _mainDishes.reversed()
            else -> filterDishesByWord(_mainDishes,filterType)
        }
    }



    fun filterTitles(filterType: String) {
        _showTitles = when (filterType) {
            "None" -> emptyList()
            "All" -> mainTitles
            "Reversed" -> mainTitles.reversed()
            else -> mainTitles
        }
    }



    private fun filterDishesByWord(list : List<DishUIState>, filterType: String ) : List<DishUIState>{
        val mainList = mutableListOf<DishUIState>()
        for(item in list){
            if(item.tags.contains(filterType)){
                mainList.add(item)
            }
        }
        return mainList
    }



    fun setMainCategoryInViewModel(category : CategoryUIState) {
        Log.d(TAG, "setMainCategoryInViewModel : data - $category")
        _mainCategory = category
    }

    fun setListDishesInViewModel(list : List<DishUIState>) {
        Log.d(TAG, "setListDishesInViewModel : list - $list")
        _mainDishes = list
    }

    fun setListTitlesInViewModel(list : List<TitleUIState>) {
        Log.d(TAG, "setListTitlesInViewModel : list - $list")
        _mainTitles = list
    }



    fun refreshDishes() : Boolean {
        var res = false
        viewModelScope.launch {
            Log.d(TAG, "refreshDishes : mainCategory - $mainCategory")
            if(mainCategory != null) {
                val resultRefresh = async { dishesRepository.refreshDishesData(mainCategory!!.id) }.await()

                Log.d(TAG, "    refreshDishes : resultRefresh - ${resultRefresh.getOrNull()}")
                res = resultRefresh.isSuccess
            }
        }
        return res
    }



    fun refreshTitles(): Boolean{
        var res = false

        var listTitlesNames = mutableListOf<String>()
        val listTitles = mutableListOf<TitleUIState>()

        viewModelScope.launch {

            for (elem in mainDishes) {
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

            //make first element of list selected
            if(listTitles.isNotEmpty()) {
                listTitles[0].isSelected = true
            }

            providerTitles.value = listTitles
            Log.d(TAG, "refreshTitles : mainTitles - $listTitles")
            res = true
        }
        return res
    }

}