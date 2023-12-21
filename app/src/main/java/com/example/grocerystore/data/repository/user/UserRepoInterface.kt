package com.example.grocerystore.data.repository.user


import com.example.grocerystore.data.helpers.UIstates.user.UserUIState
import kotlinx.coroutines.flow.Flow

interface UserRepoInterface {

    suspend fun getUserById(id: String,hard: Boolean = true): Result<UserUIState?>
    fun getCurrentUserFlow(): Flow<UserUIState?>
    suspend fun getCurrentUser(): Result<UserUIState?>

    suspend fun refreshData() : Result<Boolean?>
    suspend fun hardRefreshData(): Result<Boolean?>

    suspend fun login(userId: String, rememberMe: Boolean): Result<Boolean?>
    suspend fun singUp(userData: UserUIState, rememberMe: Boolean) : Result<Boolean?>

    suspend fun addUserLocalSource(userData: UserUIState,) : Result<Boolean?>
    suspend fun deleteUserLocalSource(userId: String,): Result<Boolean?>

    suspend fun singOut(userId: String) : Result<Boolean?>

    suspend fun checkLogin(email: String, password: String,hard : Boolean): Result<UserUIState?>
}