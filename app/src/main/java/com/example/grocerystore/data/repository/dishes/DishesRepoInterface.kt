package com.example.grocerystore.data.repository.dishes

import com.example.grocerystore.domain.models.item.DishUIState
import kotlinx.coroutines.flow.Flow

interface DishesRepoInterface{
    suspend fun refreshDishesData(dishListId : String): Result<Boolean?>

    fun getDishesListFlow(): Flow<List<DishUIState>>
    suspend fun getDishesList(): List<DishUIState>
    fun getDishByIdFlow(dishId : String) : Flow<DishUIState?>
    suspend fun getDishById(dishId : String) : DishUIState?
}