package com.example.grocerystore.data.repository.categories

import com.example.grocerystore.data.helpers.UIstates.item.CategoryUIState

interface CategoriesRepoInterface {

    suspend fun refreshCategoriesData() : Boolean
    suspend fun observeCategoryItemById(categoryId : String) : CategoryUIState?
    suspend fun observeListCategories(): List<CategoryUIState>
}