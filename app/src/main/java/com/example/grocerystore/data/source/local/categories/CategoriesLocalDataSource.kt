package com.example.grocerystore.data.source.local.categories

import com.example.grocerystore.data.helpers.UIstates.item.CategoryUIState
import com.example.grocerystore.data.source.CategoriesDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class CategoriesLocalDataSource internal constructor(
    private val categoriesDao: CategoriesDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    ) : CategoriesDataSource {

    private val TAG = "CategoriesLocalDataSource"

    override fun getListCategoriesFlow(): Flow<List<CategoryUIState>> = categoriesDao.getAllFlow()

    override suspend fun getListCategories(): List<CategoryUIState> = withContext(ioDispatcher) {
         try {
             return@withContext categoriesDao.getAll()
        } catch (e: Exception) {
             return@withContext listOf()
        }
    }

    override fun getCategoryByIdFlow(id : String) : Flow<CategoryUIState?> = categoriesDao.getItemByIdFlow(id)


    override suspend fun getCategoryById(id : String) : CategoryUIState? = withContext(ioDispatcher) {
        try {
            return@withContext categoriesDao.getItemById(id)
        } catch (e: Exception) {
            return@withContext null
        }
    }

    override suspend fun updateListCategories(categories: List<CategoryUIState>) : Unit = withContext(ioDispatcher) {
        categoriesDao.updateList(categories)
    }

    override suspend fun deleteAllCategories(): Unit = withContext(ioDispatcher) {
        categoriesDao.clear()
    }
}