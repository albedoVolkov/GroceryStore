package com.example.grocerystore.data.repository.categories

import android.util.Log
import com.example.grocerystore.data.source.remove.retrofit.RetrofitDataSource
import com.example.grocerystore.data.helpers.UIstates.item.CategoryUIState
import com.example.grocerystore.data.source.local.categories.CategoriesLocalDataSource
import com.example.grocerystore.ConstantsSource

class CategoriesRepository(
    private val categoriesRemoteSource: RetrofitDataSource,//main source of data
    private val categoriesLocalSource: CategoriesLocalDataSource,//just holder of data
) : CategoriesRepoInterface {

    companion object {
        private const val TAG = "CategoriesRepository"
    }

    override suspend fun refreshCategoriesData() : Boolean{
        try {
            val remoteProducts = categoriesRemoteSource.categoriesAPI.getCategoriesList(ConstantsSource.END_CATEGORIES_URL_LINK)
            if (remoteProducts != null) {
                Log.d(TAG, "pro list = ${remoteProducts.items}")
                categoriesLocalSource.deleteAllItems()
                categoriesLocalSource.insertListOfItems(remoteProducts.items)
                return true
            } else {
                Log.d(TAG, "refreshCategoriesData : data = null")
            }
        } catch (e: Exception) {
            Log.d(TAG, "refreshCategoriesData: Exception occurred, ${e.message}")
        }
        return false
    }

    override suspend fun observeCategoryItemById(categoryId : Int) : CategoryUIState? {
        val res : CategoryUIState? = categoriesLocalSource.getItemById(categoryId)
        if(res != null){
            Log.d(TAG, "observeCategoryItemById : data : Success = $res")
        }else{
            Log.d(TAG, "observeCategoryItemById : data : Error = null")
        }
        return res
    }

    override suspend fun observeListCategories() : List<CategoryUIState> {
        val res : List<CategoryUIState> = categoriesLocalSource.observeItems()
        Log.d(TAG, "observeListCategories : data = $res")
        return res
    }
}