package com.example.grocerystore.ui.activityMain.fragments.firstTab.viewModels


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerystore.data.helpers.UIstates.item.CategoryUIState
import com.example.grocerystore.data.repository.categories.CategoriesRepository
import com.example.grocerystore.data.repository.user.UserRepository
import com.example.grocerystore.locateLazy
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch


class CategoriesFragmentViewModel : ViewModel() {


    private val TAG = "CategoriesFragmentViewModel"


    private val userRepository by locateLazy<UserRepository>()
    private val categoriesRepository by locateLazy<CategoriesRepository>()


    //USER
    val userData = userRepository.getCurrentUserFlow().asLiveDataFlow()


    //ITEMS
    private var _showCategories : List<CategoryUIState> = emptyList()
    val showCategories: List<CategoryUIState> get() = _showCategories

    private var _mainCategoriesNotSorted : List<CategoryUIState> = listOf()
    val mainCategoriesNotSorted: List<CategoryUIState> get() = _mainCategoriesNotSorted// this list isn't for showing and not sorted

    val mainCategories = categoriesRepository.getCategoryListFlow().asLiveDataFlow()




    private fun <T> Flow<T>.asLiveDataFlow() = shareIn(viewModelScope, SharingStarted.Lazily, replay = 1)




    fun setListCategoriesInViewModel(list : List<CategoryUIState>) {
        Log.d(TAG, "setListCategoriesInViewModel : list - $list")
        _mainCategoriesNotSorted = list
    }

    fun filterItems(filterType: String) {
        _showCategories = when (filterType) {
            "None" -> emptyList()
            "All" -> mainCategoriesNotSorted
            "Reversed" -> mainCategoriesNotSorted.reversed()
            else -> mainCategoriesNotSorted
        }
    }

    fun refreshData() : Boolean{
        var data = false
        viewModelScope.launch {
            data = async {  categoriesRepository.refreshCategoriesData()}.await()
        }
        return data
    }

}