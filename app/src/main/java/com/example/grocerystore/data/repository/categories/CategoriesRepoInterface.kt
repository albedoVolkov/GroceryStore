package com.example.grocerystore.data.repository.categories

import com.example.grocerystore.domain.models.item.CategoryUIState
import kotlinx.coroutines.flow.Flow

interface CategoriesRepoInterface {

    suspend fun refreshCategoriesData() : Boolean
    fun getCategoryListFlow(): Flow<List<CategoryUIState>>
    suspend fun getCategoryList(): List<CategoryUIState>
    fun getCategoryByIdFlow(categoryId : String) : Flow<CategoryUIState?>
    suspend fun getCategoryById(categoryId : String) : CategoryUIState?
}