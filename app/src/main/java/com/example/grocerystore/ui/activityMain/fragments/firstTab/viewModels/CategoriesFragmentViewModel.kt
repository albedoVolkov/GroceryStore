package com.example.grocerystore.ui.activityMain.fragments.firstTab.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerystore.data.helpers.UIstates.item.CategoryUIState
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState
import com.example.grocerystore.data.repository.categories.CategoriesRepoInterface
import com.example.grocerystore.data.repository.user.UserRepoInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class CategoriesFragmentViewModel(private val categoriesRepository : CategoriesRepoInterface, private val userRepository: UserRepoInterface) : ViewModel() {


    companion object{
        private const val TAG = "CategoriesFragmentViewModel"
    }

    //USER
    private val _userData = MutableLiveData<UserUIState?>()
    val userData: LiveData<UserUIState?> get() = _userData


    //PRODUCTS
    private var _showCategories : MutableLiveData<List<CategoryUIState>> = MutableLiveData<List<CategoryUIState>>()
    val showCategories: LiveData<List<CategoryUIState>> get() = _showCategories

    private var _mainCategories : MutableLiveData<List<CategoryUIState>> = MutableLiveData<List<CategoryUIState>>()// this list isn't for showing and not sorted

    private var _filterType = MutableLiveData("All")
    val filterType: LiveData<String> get() = _filterType

    init{
        getCurrentUserData()

        getCategories()
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


    fun filterCategories(filterType: String) {
        Log.d(TAG, "filterType is $filterType and list is ${_mainCategories.value}")
        _showCategories.value = when (filterType) {
            "None" -> emptyList()
            "All" -> _mainCategories.value ?: emptyList()
            else -> {
                val lengthComparator =
                    Comparator { i1: CategoryUIState, i2: CategoryUIState -> (i1.id).toInt() - (i2.id).toInt()  }
                _mainCategories.value?.sortedWith(lengthComparator)
            }
        }
    }


    private fun getCategories() {
        viewModelScope.launch {
            val dataRefresh = async {  categoriesRepository.refreshCategoriesData()}

            if(dataRefresh.await()) {
                val list = async { categoriesRepository.observeListCategories() }.await() //.observeListCategories()//categoriesRepository.observeCategories().value
                Log.d(TAG, "getCategories: data : Done = $list")
                _mainCategories.value = list
            }else{
                Log.d(TAG, "getCategories: data : Error = null")
                _mainCategories.value = emptyList()
            }
            _showCategories.value = _mainCategories.value
        }
    }



    fun getCategoryById(categoryId: String): CategoryUIState? {
        var data : CategoryUIState? = null
        viewModelScope.launch {
            val dataRefresh = async { categoriesRepository.observeCategoryItemById(categoryId)}
            data = if(dataRefresh.await() != null) {
                Log.d(TAG, "getCategoryById: data : Success = ${dataRefresh.await()}")
                dataRefresh.await()
            }else{
                Log.d(TAG, "getCategoryById: data : Error = null")
                null
            }
        }
        return data
    }

}