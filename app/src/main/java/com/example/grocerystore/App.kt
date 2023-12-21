package com.example.grocerystore

import android.app.Application
import android.content.Context
import com.example.grocerystore.data.repository.cart.CartRepository
import com.example.grocerystore.data.repository.categories.CategoriesRepository
import com.example.grocerystore.data.repository.dishes.DishesRepository
import com.example.grocerystore.data.repository.user.UserRepository
import com.example.grocerystore.data.source.local.GroceryStoreDatabase
import com.example.grocerystore.data.source.local.categories.CategoriesLocalDataSource
import com.example.grocerystore.data.source.local.dishes.DishesLocalDataSource
import com.example.grocerystore.data.source.local.user.UserLocalDataSource
import com.example.grocerystore.data.source.remove.firebase.UserRemoteDataSource
import com.example.grocerystore.data.source.remove.retrofit.RetrofitDataSource
import com.example.grocerystore.services.CheckNetworkConnection
import com.example.grocerystore.services.IdService
import com.example.grocerystore.services.SessionManager

class App() : Application() {

    override fun onCreate() {
        super.onCreate()

        ServiceLocator.init()

        ServiceLocator.register<Context>(this)
        ServiceLocator.register<Application>(this)
        ServiceLocator.register(GroceryStoreDatabase.getDataBase(locate()))

        ServiceLocator.register(RetrofitDataSource)

        ServiceLocator.register(SessionManager(locate()))
        ServiceLocator.register(IdService())
        ServiceLocator.register(CheckNetworkConnection(locate()))

        ServiceLocator.register((locate() as GroceryStoreDatabase).userDao())
        ServiceLocator.register((locate() as GroceryStoreDatabase).categoriesDao())
        ServiceLocator.register((locate() as GroceryStoreDatabase).dishesDao())

        ServiceLocator.register(UserLocalDataSource((locate())))
        ServiceLocator.register(UserRemoteDataSource())

        ServiceLocator.register(CategoriesLocalDataSource(locate()))
        ServiceLocator.register(DishesLocalDataSource(locate()))

        ServiceLocator.register(UserRepository(locate(),locate(),locate()))
        ServiceLocator.register(CategoriesRepository(locate(),locate()))
        ServiceLocator.register(DishesRepository(locate(),locate()))
        ServiceLocator.register(CartRepository(locate(),locate(), locate()))
    }

}

inline fun <reified T: Any> locate() = ServiceLocator.get(T::class)

inline fun <reified T: Any> locateLazy() : Lazy<T> = lazy{ServiceLocator.get(T::class)}