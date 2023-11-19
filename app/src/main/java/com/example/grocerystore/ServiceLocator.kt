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
import com.example.grocerystore.data.source.remove.firebase.RemoteDataSource
import com.example.grocerystore.data.source.remove.firebase.UserRemoteDataSource
import com.example.grocerystore.services.CheckNetworkConnection
import com.example.grocerystore.services.CreatingNewIdsService
import com.example.grocerystore.services.ShoppingAppSessionManager

object ServiceLocator {


    @Volatile
    private var localDataSource: GroceryStoreDatabase? = null

    @Volatile
    private var remoteDataBase: RemoteDataSource? = null




    @Volatile
    private var userRemoteDataBase: UserRemoteDataSource? = null



    @Volatile
    private var sessionManager: ShoppingAppSessionManager? = null

    @Volatile
    private var networkConnectionManager: CheckNetworkConnection? = null

    @Volatile
    private var creatingIdsService: CreatingNewIdsService? = null


    private val lock = Any()

    @Volatile
    var dishesRepository: DishesRepoInterface? = null

    @Volatile
    var categoriesRepository: CategoriesRepoInterface? = null

    @Volatile
    var userRepository: UserRepoInterface? = null




    fun provideCreatingNewIdsService(context: Context): CreatingNewIdsService {
        synchronized(this) {
            return creatingIdsService ?: createCreatingNewIdsService(context)
        }
    }

    fun provideNetworkConnectionManager(application: Application): CheckNetworkConnection {
        synchronized(this) {
            return networkConnectionManager ?: createNetworkConnectionManager(application)
        }
    }

    fun provideSessionManager(context: Context): ShoppingAppSessionManager {
        synchronized(this) {
            return sessionManager ?: createSessionManager(context)
        }
    }






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

    fun provideUserRepository(context: Context): UserRepoInterface {
        synchronized(this) {
            return userRepository ?: createUserRepository(context)
        }
    }







    private fun provideLocalDataBase(context: Context): GroceryStoreDatabase {
        synchronized(this) {
            return localDataSource ?: createLocalDataBase(context)
        }
    }

    private fun provideUserRemoteDataBase(context: Context): UserRemoteDataSource {
        synchronized(this) {
            return userRemoteDataBase ?: createUserRemoteDataSource(context)
        }
    }






    fun resetApp() {
        synchronized(lock) {
            localDataSource?.apply {
                clearAllTables()
                close()
            }

            sessionManager?.deleteLoginSession()


            userRemoteDataBase = null

            localDataSource = null
            remoteDataBase = null
            sessionManager = null

            creatingIdsService = null

            categoriesRepository = null
            dishesRepository = null
            userRepository = null
        }
    }










    private fun createCreatingNewIdsService(context: Context): CreatingNewIdsService {
        val service = CreatingNewIdsService()//fireBase
        creatingIdsService = service
        return service
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
        localDataSource = GroceryStoreDatabase.getDataBase(context)
        return localDataSource!!
    }

    private fun createSessionManager(context: Context): ShoppingAppSessionManager {
        sessionManager = ShoppingAppSessionManager(context)
        return sessionManager!!
    }

    private fun createUserRemoteDataSource(context: Context): UserRemoteDataSource {
        userRemoteDataBase = UserRemoteDataSource()
        return userRemoteDataBase!!
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