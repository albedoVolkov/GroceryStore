package com.example.grocerystore.ui.activityMain.fragments.firstTab.viewModels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerystore.data.helpers.UIstates.item.CategoryUIState
import com.example.grocerystore.data.repository.categories.CategoriesRepository
import com.example.grocerystore.data.repository.user.UserRepository
import com.example.grocerystore.locateLazy
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch


class CategoriesFragmentViewModel() : ViewModel() {


    private val TAG = "CategoriesFragmentViewModel"


    private val userRepository by locateLazy<UserRepository>()
    private val categoriesRepository by locateLazy<CategoriesRepository>()


    //USER
    val userData = userRepository.getCurrentUserFlow().shareIn(viewModelScope, SharingStarted.Lazily, replay = 1)


    //ITEMS
    private var _showCategories : List<CategoryUIState> = emptyList<CategoryUIState>()
    val showCategories: List<CategoryUIState> get() = _showCategories

    val mainCategories = categoriesRepository.getCategoryListFlow().shareIn(viewModelScope, SharingStarted.Eagerly, replay = 1)// this list isn't for showing and not sorted



    init {
        refreshData()
    }


    suspend fun filterItems(filterType: String) {
        _showCategories = when (filterType) {
            "None" -> emptyList<CategoryUIState>()
            "All" -> mainCategories.last()
            "Reversed" -> mainCategories.last().reversed()
            else -> mainCategories.last()
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