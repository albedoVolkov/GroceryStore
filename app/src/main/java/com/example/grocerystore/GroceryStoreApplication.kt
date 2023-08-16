package com.example.grocerystore

import android.content.Context
import com.example.grocerystore.data.repository.CategoriesRepoInterface
import com.example.grocerystore.data.repository.DishesRepoInterface

class GroceryStoreApplication(context : Context){
    private val innerContext = context
    val dishesRepository: DishesRepoInterface
        get() = ServiceLocator.provideDishesRepository(innerContext)

    val categoryRepository : CategoriesRepoInterface
        get() = ServiceLocator.provideCategoriesRepository(innerContext)
}