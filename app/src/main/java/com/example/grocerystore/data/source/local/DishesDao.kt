package com.example.grocerystore.data.source.local

import androidx.annotation.NonNull
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.grocerystore.data.helpers.DishUIState
@Dao
interface DishesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(dishes: List<DishUIState>)

    @NonNull
    @Query("SELECT * FROM dishesList")
    suspend fun getAll(): List<DishUIState>

    @NonNull
    @Query("SELECT * FROM dishesList WHERE Id = :id")
    suspend fun getItemById(id : Int): DishUIState?

	@Query("DELETE FROM dishesList")
	suspend fun deleteAll(): Int
}