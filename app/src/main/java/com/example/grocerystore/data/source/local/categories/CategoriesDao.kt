package com.example.grocerystore.data.source.local.categories


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.grocerystore.domain.models.item.CategoryUIState
import com.example.grocerystore.domain.utils.ConstantsDomain
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriesDao {

    @Insert(CategoryUIState::class,onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(categories: List<CategoryUIState>)

    @Query("delete from ${ConstantsDomain.CATEGORIES_ROOM_TABLE_NAME}")
    suspend fun clear()


    @Query("SELECT * FROM ${ConstantsDomain.CATEGORIES_ROOM_TABLE_NAME}")
    fun getAllFlow(): Flow<List<CategoryUIState>>

    @Query("SELECT * FROM ${ConstantsDomain.CATEGORIES_ROOM_TABLE_NAME}")
    suspend fun getAll(): List<CategoryUIState>

    @Query("SELECT * FROM ${ConstantsDomain.CATEGORIES_ROOM_TABLE_NAME} WHERE id =:id LIMIT 1")
    fun getItemByIdFlow(id : String): Flow<CategoryUIState?>

    @Query("SELECT * FROM ${ConstantsDomain.CATEGORIES_ROOM_TABLE_NAME} WHERE id =:id LIMIT 1")
    suspend fun getItemById(id : String): CategoryUIState?

}