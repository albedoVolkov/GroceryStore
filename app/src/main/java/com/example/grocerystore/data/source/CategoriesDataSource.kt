package com.example.grocerystore.data.source

import com.example.grocerystore.domain.models.item.CategoryUIState
import kotlinx.coroutines.flow.Flow


interface CategoriesDataSource {

    fun getListCategoriesFlow(): Flow<List<CategoryUIState>>

    suspend fun getListCategories(): List<CategoryUIState>

    fun getCategoryByIdFlow(id : String) : Flow<CategoryUIState?>

    suspend fun getCategoryById(id : String) : CategoryUIState?

    suspend fun updateListCategories(categories: List<CategoryUIState>)

    suspend fun deleteAllCategories()

}