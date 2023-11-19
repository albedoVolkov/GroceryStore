package com.example.grocerystore.data.source

import com.example.grocerystore.data.helpers.UIstates.item.DishUIState

interface DishesDataSource {

    suspend fun observeItems(): List<DishUIState>
    suspend fun insertListOfItems(dishes: List<DishUIState>)
    suspend fun deleteAllItems()
    suspend fun getItemById(id : String) : DishUIState?
}