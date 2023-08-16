package com.example.grocerystore.data.source

import com.example.grocerystore.data.helpers.CategoryUIState


interface CategoriesDataSource {

    suspend fun observeItems(): List<CategoryUIState>
    suspend fun insertListOfItems(categories: List<CategoryUIState>)
    suspend fun deleteAllItems()
    suspend fun getItemById(id : Int) : CategoryUIState?

}