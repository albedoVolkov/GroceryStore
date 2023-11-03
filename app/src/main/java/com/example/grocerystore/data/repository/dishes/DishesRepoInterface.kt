package com.example.grocerystore.data.repository.dishes

import com.example.grocerystore.data.helpers.UIstates.item.DishUIState

interface DishesRepoInterface{
    fun setDishListId(dishListId : Int)
    suspend fun refreshDishesData(): Boolean
    suspend fun observeDishItemById(dishId : Int) : DishUIState?
    suspend fun observeListDishes(): List<DishUIState>
}