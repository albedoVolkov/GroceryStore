package com.example.grocerystore.ui.activityMain.fragments.firstTab.viewModels


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


class CategoriesFragmentViewModel() : ViewModel() {


    private val TAG = "CategoriesFragmentViewModel"


    private val userRepository by locateLazy<UserRepository>()
    private val categoriesRepository by locateLazy<CategoriesRepository>()


    //USER
    val userData = userRepository.getCurrentUserFlow().asLiveDataFlow()


    //ITEMS
    private var _showCategories : List<CategoryUIState> = emptyList()
    val showCategories: List<CategoryUIState> get() = _showCategories

    val mainCategories = categoriesRepository.getCategoryListFlow().asLiveDataFlow() // this list isn't for showing and not sorted

    private fun <T> Flow<T>.asLiveDataFlow() = shareIn(viewModelScope, SharingStarted.Lazily, replay = 1)

    init {
        refreshData()
    }


    fun filterItems(filterType: String,list : List<CategoryUIState>) {
        _showCategories = when (filterType) {
            "None" -> emptyList()
            "All" -> list
            "Reversed" -> list.reversed()
            else -> list
        }
    }

    private fun refreshData() : Boolean{
        var data = false
        viewModelScope.launch {
            data = async {  categoriesRepository.refreshCategoriesData()}.await()
        }
        return data
    }

}