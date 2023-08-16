package com.example.grocerystore.data.source.local

import com.example.grocerystore.data.helpers.CategoryUIState
import com.example.grocerystore.data.source.CategoriesDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "CategoriesLocalDataSource"

class CategoriesLocalDataSource internal constructor(
    private val categoriesDao: CategoriesDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    ) : CategoriesDataSource {

    override suspend fun observeItems(): List<CategoryUIState> = withContext(ioDispatcher) {
        return@withContext try {
            categoriesDao.getAll()
        } catch (e: Exception) {
            listOf()
        }
    }

    override suspend fun getItemById(id : Int) : CategoryUIState? = withContext(ioDispatcher) {
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

    override suspend fun insertListOfItems(categories: List<CategoryUIState>) : Unit = withContext(ioDispatcher) {
        categoriesDao.insertList(categories)
    }

    override suspend fun deleteAllItems(): Unit = withContext(ioDispatcher) {
        categoriesDao.deleteAll()
    }
}
