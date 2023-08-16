package com.example.grocerystore

import android.content.Context
import com.example.grocerystore.data.source.local.CategoriesLocalDataSource
import com.example.grocerystore.data.source.local.DishesLocalDataSource
import com.example.grocerystore.data.source.local.GroceryStoreDatabase
import com.example.grocerystore.data.source.remove.retrofit.RetrofitDataSource
import com.example.grocerystore.data.repository.CategoriesRepoInterface
import com.example.grocerystore.data.repository.DishesRepoInterface
import com.example.grocerystore.data.repository.CategoriesRepository
import com.example.grocerystore.data.repository.DishesRepository

object ServiceLocator {
    private var database: GroceryStoreDatabase? = null
    private val lock = Any()

    @Volatile
    var dishesRepository: DishesRepoInterface? = null

    @Volatile
    var categoriesRepository: CategoriesRepoInterface? = null

    fun provideDishesRepository(context: Context): DishesRepoInterface {
        synchronized(this) {
            return dishesRepository ?: createDishesRepository(context)
        }
    }

    fun provideCategoriesRepository(context: Context): CategoriesRepoInterface {
        synchronized(this) {
            return categoriesRepository ?: createCategoriesRepository(context)
        }
    }

    fun resetRepository() {
        synchronized(lock) {
            database?.apply {
                clearAllTables()
                close()
            }
            database = null
            categoriesRepository = null
            dishesRepository = null
        }
    }

    private fun createCategoriesRepository(context: Context): CategoriesRepoInterface {
        val newRepo =
            CategoriesRepository(
                RetrofitDataSource,
                createCategoriesLocalDataSource(context)
            )
        categoriesRepository = newRepo
        return newRepo
    }

    private fun createDishesRepository(context: Context): DishesRepoInterface {
        val newRepo =
            DishesRepository(
                RetrofitDataSource,
                createDishesLocalDataSource(context),
            )
        dishesRepository = newRepo
        return newRepo
    }

    private fun createCategoriesLocalDataSource(context: Context): CategoriesLocalDataSource {
        val database = database ?: GroceryStoreDatabase.getDataBase(context.applicationContext)
        return CategoriesLocalDataSource(database.categoriesDao())
    }

    private fun createDishesLocalDataSource(context: Context): DishesLocalDataSource {
        val database = database ?: GroceryStoreDatabase.getDataBase(context.applicationContext)
        return DishesLocalDataSource(database.dishesDao())
    }
}