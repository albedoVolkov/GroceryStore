package com.example.grocerystore.data.source.local.user

import androidx.room.Dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.grocerystore.domain.models.user.UserUIState
import com.example.grocerystore.domain.utils.ConstantsDomain
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {


        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insert(user: UserUIState)

        @Query("delete from ${ConstantsDomain.USER_ROOM_TABLE_NAME} where userId =:userId")
        suspend fun deleteById(userId: String)

        @Query("select * from ${ConstantsDomain.USER_ROOM_TABLE_NAME} where userId =:userId limit 1")
        suspend fun getById(userId: String): UserUIState?

        @Query("select * from ${ConstantsDomain.USER_ROOM_TABLE_NAME} where email =:email limit 1")
        suspend fun getByEmail(email : String): UserUIState?

        @Query("select * from ${ConstantsDomain.USER_ROOM_TABLE_NAME}")
        suspend fun getListOfUsers(): List<UserUIState>

        @Query("delete from ${ConstantsDomain.USER_ROOM_TABLE_NAME}")
        suspend fun clear()

        @Query("select * from ${ConstantsDomain.USER_ROOM_TABLE_NAME}")
        fun getListOfUsersFlow(): Flow<List<UserUIState>>

        @Query("select * from ${ConstantsDomain.USER_ROOM_TABLE_NAME} where userId =:userId limit 1")
        fun getByIdFlow(userId: String): Flow<UserUIState?>



}