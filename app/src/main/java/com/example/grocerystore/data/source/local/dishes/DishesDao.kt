package com.example.grocerystore.data.source.local.dishes

import androidx.annotation.NonNull
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import com.example.grocerystore.data.helpers.UIstates.item.DishUIState
import kotlinx.coroutines.flow.Flow

@Dao
interface DishesDao {

    @Update
    suspend fun updateList(dishes: List<DishUIState>)

    @Query("delete from DishesListRoom")
    suspend fun clear()



    @Query("SELECT * FROM DishesListRoom")
    fun getAllFlow(): Flow<List<DishUIState>>

    @NonNull
    @Query("SELECT * FROM DishesListRoom")
    suspend fun getAll(): List<DishUIState>

    @NonNull
    @Query("SELECT * FROM DishesListRoom WHERE id =:id LIMIT 1")
    fun getItemByIdFlow(id : String): Flow<DishUIState?>

    @NonNull
    @Query("SELECT * FROM DishesListRoom WHERE id =:id LIMIT 1")
    suspend fun getItemById(id : String): DishUIState?

}
