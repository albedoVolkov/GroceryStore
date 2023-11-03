package com.example.grocerystore.data.source.local.user

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState

@Dao
interface UserDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insert(user: UserUIState)//

        @Query("SELECT * FROM users WHERE userId = :userId")
        suspend fun getById(userId: String): UserUIState?

        @Query("SELECT * FROM users WHERE email = :email")
        suspend fun getByEmail(email : String): UserUIState?

        @Update(entity = UserUIState::class)
        suspend fun update(obj: UserUIState)//

        @Query("DELETE FROM users WHERE userId = :userId" )
        suspend fun delete(userId: String)//

        @Query("SELECT * FROM users" )
        fun getAll() : LiveData<List<UserUIState>>//

        @Query("DELETE FROM users")
        suspend fun clear(): Int//
}