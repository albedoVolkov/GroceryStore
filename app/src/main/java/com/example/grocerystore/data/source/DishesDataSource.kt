package com.example.grocerystore.data.source

import com.example.grocerystore.domain.models.item.DishUIState
import kotlinx.coroutines.flow.Flow

interface DishesDataSource {

    fun getListDishesFlow(): Flow<List<DishUIState>>

    suspend fun getListDishes(): List<DishUIState>

    fun getDishByIdFlow(id : String) : Flow<DishUIState?>

    suspend fun getDishById(id : String) : DishUIState?

    suspend fun updateListDishes(dishes: List<DishUIState>)

    suspend fun deleteAllDishes()

}