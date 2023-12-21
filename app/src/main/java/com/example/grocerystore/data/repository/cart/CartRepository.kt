package com.example.grocerystore.data.repository.cart

import android.util.Log
import com.example.grocerystore.data.helpers.UIstates.item.CartUIState
import com.example.grocerystore.data.source.local.user.UserLocalDataSource
import com.example.grocerystore.data.source.remove.firebase.UserRemoteDataSource
import com.example.grocerystore.services.SessionManager
import kotlinx.coroutines.flow.Flow

class CartRepository(
    private val remoteSource: UserRemoteDataSource,
    private val localSource: UserLocalDataSource,
    private val sessionManager: SessionManager,
)  : CartRepoInterface {


    private val TAG = "CartRepository"


    override suspend fun getCartsById(userId: String): Result<List<CartUIState>> {
        Log.d(TAG, " getCartsById : userId = $userId")
        return localSource.getUserCartsById(userId)
    }

    override fun getCurrentUserCartsFlow(): Flow<List<CartUIState>> = localSource.getUserCartsByIdFlow(sessionManager.getUserId())


    override suspend fun addCartInBasketOfCurrentUser(newItem: CartUIState): Result<Boolean?> {
        Log.d(TAG, " addCartInBasketOfCurrentUser : newItem = $newItem , userId = ${sessionManager.getUserId()}")
        return localSource.insertCartItem(newItem, sessionManager.getUserId())
    }


    override suspend fun deleteCartInBasketOfCurrentUser(itemId: String): Result<Boolean?>  {
            Log.d(TAG, " deleteCartInBasketOfCurrentUser : itemId = $itemId , userId = ${sessionManager.getUserId()}")
            return localSource.deleteCartItem(itemId,sessionManager.getUserId())
    }


    override suspend fun updateCartInBasketOfCurrentUser(updatedItem: CartUIState): Result<Boolean?>{
        Log.d(TAG, " updateCartInBasketOfCurrentUser : updatedItem = $updatedItem , userId = ${sessionManager.getUserId()}")
        return localSource.updateCartItem(updatedItem,sessionManager.getUserId())
    }










    override suspend fun addCartInBasketOfUser(newItem: CartUIState, userId : String): Result<Boolean?> {
        Log.d(TAG, " addCartInBasketOfUser : newItem = $newItem , userId = $userId")
        return localSource.insertCartItem(newItem, userId)
    }


    override suspend fun deleteCartInBasketOfUser(itemId: String, userId : String): Result<Boolean?>  {
        Log.d(TAG, " deleteCartInBasketOfUser : itemId = $itemId , userId = $userId")
        return localSource.deleteCartItem(itemId,userId)
    }


    override suspend fun updateCartInBasketOfUser(updatedItem: CartUIState, userId : String): Result<Boolean?>{
        Log.d(TAG, " updateCartInBasketOfUser : updatedItem = $updatedItem , userId = $userId")
        return localSource.updateCartItem(updatedItem,userId)
    }
}