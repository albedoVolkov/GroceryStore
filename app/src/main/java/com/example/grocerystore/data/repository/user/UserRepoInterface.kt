package com.example.grocerystore.data.repository.user

import com.example.grocerystore.data.helpers.UIstates.item.CartUIState
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState

interface UserRepoInterface {

    suspend fun getUserById(id: String,hard: Boolean = true): Result<UserUIState?>
    suspend fun getCurrentUser(): Result<UserUIState?>

    suspend fun refreshData() : Result<Boolean?>
    suspend fun hardRefreshData(): Result<Boolean?>

    suspend fun login(userId: String, rememberMe: Boolean): Result<Boolean?>
    suspend fun singUp(userData: UserUIState, rememberMe: Boolean) : Result<Boolean?>

    suspend fun addUserLocalSource(userData: UserUIState,) : Result<Boolean?>
    suspend fun deleteUserLocalSource(userId: String,): Result<Boolean?>

    suspend fun singOut(userId: String) : Result<Boolean?>

    suspend fun checkLogin(email: String, password: String,hard : Boolean): Result<UserUIState?>

    suspend fun addProductInBasketOfUser(newItem: CartUIState, userId: String): Result<Boolean?>
    suspend fun deleteProductInBasketOfUser(itemId: String, userId: String): Result<Boolean?>
    suspend fun updateProductInBasketOfUser(updatedItem: CartUIState, userId: String): Result<Boolean?>
}