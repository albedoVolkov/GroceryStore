package com.example.grocerystore.data.source.local.categories


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.grocerystore.data.helpers.UIstates.item.CategoryUIState
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriesDao {

    @Insert(CategoryUIState::class,onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(categories: List<CategoryUIState>)

    @Query("delete from CategoriesListRoom")
    suspend fun clear()


    @Query("SELECT * FROM CategoriesListRoom")
    fun getAllFlow(): Flow<List<CategoryUIState>>

    @Query("SELECT * FROM CategoriesListRoom")
    suspend fun getAll(): List<CategoryUIState>

    @Query("SELECT * FROM CategoriesListRoom WHERE id =:id LIMIT 1")
    fun getItemByIdFlow(id : String): Flow<CategoryUIState?>

    @Query("SELECT * FROM CategoriesListRoom WHERE id =:id LIMIT 1")
    suspend fun getItemById(id : String): CategoryUIState?

}