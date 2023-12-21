package com.example.grocerystore.data.source

import com.example.grocerystore.data.helpers.EmailData
import com.example.grocerystore.data.helpers.UIstates.item.AddressUIState
import com.example.grocerystore.data.helpers.UIstates.item.CartUIState
import com.example.grocerystore.data.helpers.UIstates.item.OrderUIState
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState
import kotlinx.coroutines.flow.Flow

interface UserDataSource {


	suspend fun updateEmail(email: String, userId: String): Result<Boolean>
	suspend fun updateAddress(newAddress: List<AddressUIState>, userId: String): Result<Boolean>

	suspend fun likeProduct(productId: String, userId: String): Result<Boolean>
	suspend fun dislikeProduct(productId: String, userId: String): Result<Boolean>

	suspend fun insertCartItem(newItem: CartUIState, userId: String): Result<Boolean>
	suspend fun updateCartItem(item: CartUIState, userId: String): Result<Boolean>
	suspend fun deleteCartItem(itemId: String, userId: String): Result<Boolean>
	suspend fun emptyCartItem(userId: String): Result<Boolean>


	suspend fun addUser(userData: UserUIState): Result<Boolean>
	suspend fun deleteUser(userId: String): Result<Boolean>
	suspend fun clearUser(): Result<Boolean>

	suspend fun placeOrder(newOrder: OrderUIState, userId: String): Result<Boolean>

	suspend fun setStatusOfOrderByUserId(orderId: String, userId: String, status: String): Result<Boolean>
	fun getAllUsersFlow(): Flow<List<UserUIState>>
	suspend fun getAllUsers(): Result<List<UserUIState>>

	suspend fun getUserCartsById(userId: String): Result<List<CartUIState>>
	fun getUserCartsByIdFlow(userId: String): Flow<List<CartUIState>>

	fun getUserByIdFlow(userId: String): Flow<UserUIState?>
	suspend fun getUserById(userId: String): Result<UserUIState?>

	suspend fun getEmails(): Result<EmailData?> //getting email from firebase
	suspend fun getUserByEmailAndPassword(email: String, password: String): Result<UserUIState?>
	suspend fun getUserByEmail(emailAddress: String): Result<UserUIState?>
}