package com.example.grocerystore.data.source

import com.example.grocerystore.data.helpers.UIstates.user.UserUIState
import kotlinx.coroutines.flow.Flow

interface UserDataSource {

	suspend fun addUser(userData: UserUIState): Result<Boolean>
	suspend fun deleteUser(userId: String): Result<Boolean>
	suspend fun updateUser(newUser: UserUIState): Result<Boolean>
	suspend fun clearUser(): Result<Boolean>

	fun getAllUsersFlow(): Flow<List<UserUIState>>
	suspend fun getAllUsers(): Result<List<UserUIState>>

	fun getUserByIdFlow(userId: String): Flow<UserUIState?>
	suspend fun getUserById(userId: String): Result<UserUIState?>

	suspend fun getUserByEmail(emailAddress: String): Result<UserUIState?>
}