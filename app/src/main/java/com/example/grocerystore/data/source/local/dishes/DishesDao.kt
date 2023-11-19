package com.example.grocerystore.data.source.local.dishes

import androidx.annotation.NonNull
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.grocerystore.data.helpers.UIstates.item.DishUIState
@Dao
interface DishesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(dishes: List<DishUIState>)

    @NonNull
    @Query("SELECT * FROM dishes")
    suspend fun getAll(): List<DishUIState>

    @NonNull
    @Query("SELECT * FROM dishes WHERE Id = :id")
    suspend fun getItemById(id : String): DishUIState?

	@Query("DELETE FROM dishes")
	suspend fun deleteAll(): Int
}
