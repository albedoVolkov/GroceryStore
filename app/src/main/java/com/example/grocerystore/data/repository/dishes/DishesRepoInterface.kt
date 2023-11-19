package com.example.grocerystore.data.repository.dishes

import com.example.grocerystore.data.helpers.UIstates.item.DishUIState

interface DishesRepoInterface{
    fun setDishListId(dishListId : String)
    suspend fun refreshDishesData(): Boolean
    suspend fun observeDishItemById(dishId : String) : DishUIState?
    suspend fun observeListDishes(): List<DishUIState>
}