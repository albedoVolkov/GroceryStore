package com.example.grocerystore.data.repository

import com.example.grocerystore.data.helpers.DishUIState

interface DishesRepoInterface{
    fun setDishListId(dishListId : Int)
    suspend fun refreshDishesData(): Boolean
    suspend fun observeDishItemById(dishId : Int) : DishUIState?
    suspend fun observeListDishes(): List<DishUIState>
}