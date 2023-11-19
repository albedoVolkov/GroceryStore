package com.example.grocerystore.data.source.local.categories

import com.example.grocerystore.data.helpers.UIstates.item.CategoryUIState
import com.example.grocerystore.data.source.CategoriesDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CategoriesLocalDataSource internal constructor(
    private val categoriesDao: CategoriesDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    ) : CategoriesDataSource {

    private val TAG = "CategoriesLocalDataSource"

    override suspend fun observeItems(): List<CategoryUIState> = withContext(ioDispatcher) {
        return@withContext try {
            categoriesDao.getAll()
        } catch (e: Exception) {
            listOf()
        }
    }

    override suspend fun getItemById(id : String) : CategoryUIState? = withContext(ioDispatcher) {
        try {
            val item = categoriesDao.getItemById(id)
            if (item != null) {
                return@withContext item
            } else {
                return@withContext null
            }
        } catch (e: Exception) {
            return@withContext null
        }
    }

    override suspend fun insertListOfItems(categories: List<CategoryUIState>) : Unit =
        withContext(ioDispatcher) {
            categoriesDao.insertList(categories)
        }

    override suspend fun deleteAllItems(): Unit = withContext(ioDispatcher) {
        categoriesDao.deleteAll()
    }
}