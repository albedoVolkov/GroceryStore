package com.example.grocerystore.data.source.local.categories

import androidx.annotation.NonNull
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.grocerystore.data.helpers.UIstates.item.CategoryUIState
@Dao
interface CategoriesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(categories: List<CategoryUIState>)

    @NonNull
    @Query("SELECT * FROM categoriesList")
    suspend fun getAll(): List<CategoryUIState>

    @NonNull
    @Query("SELECT * FROM categoriesList WHERE Id = :id")
    suspend fun getItemById(id : String): CategoryUIState?

    @NonNull
    @Query("DELETE FROM categoriesList")
    suspend fun deleteAll()
}