package com.example.grocerystore.data.repository.categories

import android.util.Log
import com.example.grocerystore.data.source.remove.retrofit.RetrofitDataSource
import com.example.grocerystore.data.helpers.UIstates.item.CategoryUIState
import com.example.grocerystore.data.source.local.categories.CategoriesLocalDataSource
import com.example.grocerystore.services.ConstantsSource
import kotlinx.coroutines.flow.Flow

class CategoriesRepository(
    private val remoteSource: RetrofitDataSource,//main source of data
    private val localSource: CategoriesLocalDataSource,//just holder of data
) : CategoriesRepoInterface {

    private val TAG = "CategoriesRepository"

    override suspend fun refreshCategoriesData() : Boolean{
        try {
            val remoteProducts = remoteSource.categoriesAPI.getCategoriesList(
                ConstantsSource.END_CATEGORIES_URL_LINK)
            if (remoteProducts != null) {
                Log.d(TAG, "pro list = ${remoteProducts.items}")
                localSource.updateListCategories(remoteProducts.items)
                return true
            } else {
                Log.d(TAG, "refreshCategoriesData : data = null")
            }
        } catch (e: Exception) {
            Log.d(TAG, "refreshCategoriesData: Exception occurred, ${e.message}")
        }
        return false
    }



    override fun getCategoryListFlow(): Flow<List<CategoryUIState>> = localSource.getListCategoriesFlow()

    override suspend fun getCategoryList(): List<CategoryUIState>  = localSource.getListCategories()

    override fun getCategoryByIdFlow(categoryId : String) : Flow<CategoryUIState?> = localSource.getCategoryByIdFlow(categoryId)

    override suspend fun getCategoryById(categoryId : String) : CategoryUIState? = localSource.getCategoryById(categoryId)

}