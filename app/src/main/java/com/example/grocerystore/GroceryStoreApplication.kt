package com.example.grocerystore

import android.content.Context
import com.example.grocerystore.data.repository.categories.CategoriesRepoInterface
import com.example.grocerystore.data.repository.dishes.DishesRepoInterface
import com.example.grocerystore.data.repository.user.UserRepoInterface

class GroceryStoreApplication(context: Context) {
    private val innerContext = context
    val dishesRepository: DishesRepoInterface
        get() = ServiceLocator.provideDishesRepository(innerContext)

    val categoryRepository: CategoriesRepoInterface
        get() = ServiceLocator.provideCategoriesRepository(innerContext)

    val userRepository: UserRepoInterface
        get() = ServiceLocator.provideUserRepository(innerContext)

    val sessionManager: ShoppingAppSessionManager
        get() = ServiceLocator.provideSessionManager(innerContext)

    fun resetAll(){
        ServiceLocator.resetApp()
    }
}