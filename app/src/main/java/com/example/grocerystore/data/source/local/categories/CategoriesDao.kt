package com.example.grocerystore.data.source.local.categories

import androidx.annotation.NonNull
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.example.grocerystore.data.helpers.UIstates.item.CategoryUIState
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriesDao {

    @Update
    suspend fun updateList(categories: List<CategoryUIState>)

    @Query("delete from CategoriesListRoom")
    suspend fun clear()



    @Query("SELECT * FROM CategoriesListRoom")
    fun getAllFlow(): Flow<List<CategoryUIState>>

    @NonNull
    @Query("SELECT * FROM CategoriesListRoom")
    suspend fun getAll(): List<CategoryUIState>

    @NonNull
    @Query("SELECT * FROM CategoriesListRoom WHERE id =:id LIMIT 1")
    fun getItemByIdFlow(id : String): Flow<CategoryUIState?>

    @NonNull
    @Query("SELECT * FROM CategoriesListRoom WHERE id =:id LIMIT 1")
    suspend fun getItemById(id : String): CategoryUIState?

}