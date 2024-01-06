package com.example.grocerystore.data.repository.user


import com.example.grocerystore.data.helpers.UIstates.item.CartUIState
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState
import kotlinx.coroutines.flow.Flow

interface UserRepoInterface {

    fun getUserByIdFlow(id: String, hard: Boolean): Flow<UserUIState?>
    suspend fun getUserById(id: String,hard: Boolean = true): Result<UserUIState?>

    fun getCurrentUserFlow(): Flow<UserUIState?>
    suspend fun getCurrentUser(): Result<UserUIState?>

    suspend fun refreshData() : Result<Boolean?>
    suspend fun hardRefreshData(): Result<Boolean?>

    suspend fun checkLogin(email: String, password: String,hard : Boolean): Result<UserUIState?>
    suspend fun singOut(userId: String) : Result<Boolean?>
    suspend fun login(userId: String, rememberMe: Boolean): Result<Boolean?>
    suspend fun singUp(userData: UserUIState, rememberMe: Boolean) : Result<Boolean?>

    suspend fun addUserLocalSource(userData: UserUIState,) : Result<Boolean?>
    suspend fun deleteUserLocalSource(userId: String,): Result<Boolean?>

    suspend fun addCartInBasketOfUser(newItem: CartUIState, userId : String): Result<Boolean?>
    suspend fun deleteCartInBasketOfUser(itemId: String, userId : String): Result<Boolean?>
    suspend fun updateCartInBasketOfUser(updatedItem: CartUIState, userId : String): Result<Boolean?>

    suspend fun addCartInBasketOfCurrentUser(newItem: CartUIState): Result<Boolean?>
    suspend fun deleteCartInBasketOfCurrentUser(itemId: String): Result<Boolean?>
    suspend fun updateCartInBasketOfCurrentUser(updatedItem: CartUIState): Result<Boolean?>
}