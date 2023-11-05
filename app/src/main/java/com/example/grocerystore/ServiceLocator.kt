package com.example.grocerystore

import android.app.Application
import android.content.Context
import com.example.grocerystore.data.source.local.categories.CategoriesLocalDataSource
import com.example.grocerystore.data.source.local.dishes.DishesLocalDataSource
import com.example.grocerystore.data.source.local.GroceryStoreDatabase
import com.example.grocerystore.data.source.remove.retrofit.RetrofitDataSource
import com.example.grocerystore.data.repository.categories.CategoriesRepoInterface
import com.example.grocerystore.data.repository.dishes.DishesRepoInterface
import com.example.grocerystore.data.repository.categories.CategoriesRepository
import com.example.grocerystore.data.repository.dishes.DishesRepository
import com.example.grocerystore.data.repository.user.UserRepoInterface
import com.example.grocerystore.data.repository.user.UserRepository
import com.example.grocerystore.data.source.local.user.UserLocalDataSource
import com.example.grocerystore.data.source.remove.firebase.UserRemoteDataSource

object ServiceLocator {


    @Volatile
    private var localdatabase: GroceryStoreDatabase? = null

    @Volatile
    private var remotedatabase: UserRemoteDataSource? = null

    @Volatile
    private var sessionManager: ShoppingAppSessionManager? = null

    @Volatile
    private var networkConnectionManager: CheckNetworkConnection? = null


    private val lock = Any()

    @Volatile
    var dishesRepository: DishesRepoInterface? = null

    @Volatile
    var categoriesRepository: CategoriesRepoInterface? = null

    @Volatile
    var userRepository: UserRepoInterface? = null






    fun provideDishesRepository(context: Context): DishesRepoInterface {
        synchronized(this) {
            return dishesRepository ?: createDishesRepository(context)
        }
    }

    fun provideNetworkConnectionManager(application: Application): CheckNetworkConnection {
        synchronized(this) {
            return networkConnectionManager ?: createNetworkConnectionManager(application)
        }
    }

    fun provideCategoriesRepository(context: Context): CategoriesRepoInterface {
        synchronized(this) {
            return categoriesRepository ?: createCategoriesRepository(context)
        }
    }

    fun provideUserRepository(context: Context): UserRepoInterface {
        synchronized(this) {
            return userRepository ?: createUserRepository(context)
        }
    }




    fun provideSessionManager(context: Context): ShoppingAppSessionManager {
        synchronized(this) {
            return sessionManager ?: createSessionManager(context)
        }
    }

    private fun provideLocalDataBase(context: Context): GroceryStoreDatabase {
        synchronized(this) {
            return localdatabase ?: createLocalDataBase(context)
        }
    }







    fun resetApp() {
        synchronized(lock) {
            localdatabase?.apply {
                clearAllTables()
                close()
            }

            sessionManager?.deleteLoginSession()

            localdatabase = null
            remotedatabase = null
            sessionManager = null

            categoriesRepository = null
            dishesRepository = null
            userRepository = null
        }
    }













    private fun createNetworkConnectionManager(application: Application): CheckNetworkConnection {
        val manager = CheckNetworkConnection(application)
        networkConnectionManager = manager
        return manager
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


    private fun createUserRepository(context: Context): UserRepoInterface {
        val newRepo =
            UserRepository(
                createUserRemoteDataSource(context),
                createUserLocalDataSource(context),
                provideSessionManager(context),
            )
        userRepository = newRepo
        return newRepo
    }






    private fun createLocalDataBase(context: Context): GroceryStoreDatabase {
        localdatabase = GroceryStoreDatabase.getDataBase(context)
        return localdatabase!!
    }

    private fun createSessionManager(context: Context): ShoppingAppSessionManager {
        sessionManager = ShoppingAppSessionManager(context)
        return sessionManager!!
    }

    private fun createUserRemoteDataSource(context: Context): UserRemoteDataSource {
        remotedatabase = UserRemoteDataSource()
        return remotedatabase!!
    }








    private fun createCategoriesLocalDataSource(context: Context): CategoriesLocalDataSource {
        val database = provideLocalDataBase(context)
        return CategoriesLocalDataSource(database.categoriesDao())
    }

    private fun createDishesLocalDataSource(context: Context): DishesLocalDataSource {
        val database = provideLocalDataBase(context)
        return DishesLocalDataSource(database.dishesDao())
    }

    private fun createUserLocalDataSource(context: Context): UserLocalDataSource {
        val database = provideLocalDataBase(context)
        return UserLocalDataSource(database.userDao())
    }



}