package com.example.grocerystore.ui.viewModels

import android.service.autofill.UserData
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerystore.GroceryStoreApplication
import com.example.grocerystore.data.helpers.CategoryUIState
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


private const val TAG = "CategoriesFragmentViewModel"

class CategoriesFragmentViewModel(application: GroceryStoreApplication) : ViewModel() {

    private val categoriesRepository = application.categoryRepository
//    private val authRepository =
//        (application.applicationContext as GroceryStoreApplication).authRepository

    //private val sessionManager = GroceryStoreSessionManager(application.applicationContext)
    //private val currentUser = sessionManager.getUserIdFromSession()
    //val isUserASeller = sessionManager.isUserSeller()

    private var _lastCategory = CategoryUIState()
    val lastCategory: CategoryUIState get() = _lastCategory

    private var _showCategories : MutableLiveData<List<CategoryUIState>> = MutableLiveData<List<CategoryUIState>>()
    val showCategories: LiveData<List<CategoryUIState>> get() = _showCategories

    private var _mainCategories : MutableLiveData<List<CategoryUIState>> = MutableLiveData<List<CategoryUIState>>()// this list isn't for showing and not sorted

    private var _filterType = MutableLiveData("All")
    val filterType: LiveData<String> get() = _filterType

    private val _userData = MutableLiveData<UserData?>()
    val userData: LiveData<UserData?> get() = _userData

    init {
        getCategories()
    }

    fun filterCategories(filterType: String) {
        Log.d(TAG, "filterType is $filterType and list is ${_mainCategories.value}")
        _showCategories.value = when (filterType) {
            "None" -> emptyList()
            "All" -> _mainCategories.value ?: emptyList()
            else -> {
                val lengthComparator =
                    Comparator { i1: CategoryUIState, i2: CategoryUIState -> i1.id - i2.id }
                _mainCategories.value?.sortedWith(lengthComparator)
            }
        }
    }

    fun setLastCategory(itemData : CategoryUIState){
        _lastCategory = itemData
    }

    private fun getCategories() {
        viewModelScope.launch {
            val dataRefresh = async {  categoriesRepository.refreshCategoriesData()}

            if(dataRefresh.await()) {
                val res = async { categoriesRepository.observeListCategories() } //.observeListCategories()//categoriesRepository.observeCategories().value
                val list = res.await()
                Log.d(TAG, "getCategories: data : Done = $list")
                _mainCategories.value = list
            }else{
                Log.d(TAG, "getCategories: data : Error = null")
                _mainCategories.value = emptyList()
            }
            _showCategories.value = _mainCategories.value
        }
    }



    fun getCategoryById(categoryId: Int): CategoryUIState? {
        var data : CategoryUIState? = null
        viewModelScope.launch {
            val dataRefresh = async { categoriesRepository.observeCategoryItemById(categoryId)}
            if(dataRefresh.await() != null) {
                Log.d(TAG, "getCategoryById: data : Success = ${dataRefresh.await()}")
                data = dataRefresh.await()
            }else{
                Log.d(TAG, "getCategoryById: data : Error = null")
                data = null
            }
        }
        return data
    }

//    fun getUserData() {
//        viewModelScope.launch {
//            _dataStatus.value = Utils.StoreDataStatus.LOADING
//            val deferredRes = async { authRepository.getUserData(currentUser!!) }
//            val res = deferredRes.await()
//            if (res is com.example.grocerystore.data.helpers.Result.Success<*>) {
//                val uData = res.data
//                _userData.value = uData
//                _dataStatus.value = Utils.StoreDataStatus.DONE
//            } else {
//                _dataStatus.value = Utils.StoreDataStatus.ERROR
//                _userData.value = null
//            }
//        }
//    }

}