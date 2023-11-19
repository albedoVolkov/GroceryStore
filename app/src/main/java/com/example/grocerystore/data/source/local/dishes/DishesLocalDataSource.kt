package com.example.grocerystore.data.source.local.dishes

import com.example.grocerystore.data.helpers.UIstates.item.DishUIState
import com.example.grocerystore.data.source.DishesDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DishesLocalDataSource internal constructor(
    private val dishesDao: DishesDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DishesDataSource {

    private val TAG = "DishesLocalDataSource"

    override suspend fun observeItems(): List<DishUIState> = withContext(ioDispatcher) {
        return@withContext try {
            dishesDao.getAll()
        } catch (e: Exception) {
            listOf()
        }
    }

    override suspend fun insertListOfItems(dishes: List<DishUIState>): Unit =
        withContext(ioDispatcher) {
            dishesDao.insertList(dishes)
        }

    override suspend fun deleteAllItems(): Unit = withContext(ioDispatcher) {
        dishesDao.deleteAll()
    }

    override suspend fun getItemById(id: String): DishUIState? = withContext(ioDispatcher) {
        try {
            val item = dishesDao.getItemById(id)
            if (item != null) {
                return@withContext item
            } else {
                return@withContext null
            }
        } catch (e: Exception) {
            return@withContext null
        }
    }
}