package com.example.grocerystore.data.source.local.dishes

import com.example.grocerystore.data.helpers.UIstates.item.DishUIState
import com.example.grocerystore.data.source.DishesDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class DishesLocalDataSource internal constructor(
    private val dao: DishesDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DishesDataSource {

    private val TAG = "DishesLocalDataSource"

    override fun getListDishesFlow(): Flow<List<DishUIState>> = dao.getAllFlow()

    override suspend fun getListDishes(): List<DishUIState> = withContext(ioDispatcher) {
        try {
            return@withContext dao.getAll()
        } catch (e: Exception) {
            return@withContext listOf()
        }
    }

    override fun getDishByIdFlow(id : String) : Flow<DishUIState?> = dao.getItemByIdFlow(id)


    override suspend fun getDishById(id : String) : DishUIState? = withContext(ioDispatcher) {
        try {
            return@withContext dao.getItemById(id)
        } catch (e: Exception) {
            return@withContext null
        }
    }

    override suspend fun updateListDishes(dishes: List<DishUIState>) : Unit = withContext(ioDispatcher) {
        dao.updateList(dishes)
    }

    override suspend fun deleteAllDishes(): Unit = withContext(ioDispatcher) {
        dao.clear()
    }

}