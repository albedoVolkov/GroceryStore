package com.example.grocerystore.data.repository

import com.example.grocerystore.data.helpers.CategoryUIState

interface CategoriesRepoInterface {

    suspend fun refreshCategoriesData() : Boolean
    suspend fun observeCategoryItemById(categoryId : Int) : CategoryUIState?
    suspend fun observeListCategories(): List<CategoryUIState>
}