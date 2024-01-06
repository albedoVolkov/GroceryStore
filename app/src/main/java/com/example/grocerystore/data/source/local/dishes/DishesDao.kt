package com.example.grocerystore.data.source.local.dishes

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.grocerystore.data.helpers.UIstates.item.DishUIState
import kotlinx.coroutines.flow.Flow

@Dao
interface DishesDao {

    @Insert(DishUIState::class,onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(categories: List<DishUIState>)

    @Query("delete from DishesListRoom")
    suspend fun clear()


    @Query("SELECT * FROM DishesListRoom")
    fun getAllFlow(): Flow<List<DishUIState>>

    @Query("SELECT * FROM DishesListRoom")
    suspend fun getAll(): List<DishUIState>

    @Query("SELECT * FROM DishesListRoom WHERE id =:id LIMIT 1")
    fun getItemByIdFlow(id : String): Flow<DishUIState?>

    @Query("SELECT * FROM DishesListRoom WHERE id =:id LIMIT 1")
    suspend fun getItemById(id : String): DishUIState?

}
