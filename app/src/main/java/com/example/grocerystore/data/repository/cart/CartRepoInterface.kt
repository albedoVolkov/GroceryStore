package com.example.grocerystore.data.repository.cart

import com.example.grocerystore.data.helpers.UIstates.item.CartUIState
import kotlinx.coroutines.flow.Flow

interface CartRepoInterface {

    suspend fun getCartsById(userId: String): Result<List<CartUIState>>
    fun getCurrentUserCartsFlow(): Flow<List<CartUIState>>

    suspend fun addCartInBasketOfUser(newItem: CartUIState, userId : String): Result<Boolean?>
    suspend fun deleteCartInBasketOfUser(itemId: String, userId : String): Result<Boolean?>
    suspend fun updateCartInBasketOfUser(updatedItem: CartUIState, userId : String): Result<Boolean?>

    suspend fun addCartInBasketOfCurrentUser(newItem: CartUIState): Result<Boolean?>
    suspend fun deleteCartInBasketOfCurrentUser(itemId: String): Result<Boolean?>
    suspend fun updateCartInBasketOfCurrentUser(updatedItem: CartUIState): Result<Boolean?>
}