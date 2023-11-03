package com.example.grocerystore.data.source

import com.example.grocerystore.data.helpers.EmailData
import com.example.grocerystore.data.helpers.UIstates.item.AddressUIState
import com.example.grocerystore.data.helpers.UIstates.item.CartUIState
import com.example.grocerystore.data.helpers.UIstates.item.OrderUIState
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState

interface UserDataSource {


	suspend fun updateEmail(email: String, userId: String)
	suspend fun updateAddress(newAddress: AddressUIState, userId: String)

	suspend fun likeProduct(productId: Int, userId: String)
	suspend fun dislikeProduct(productId: Int, userId: String)

	suspend fun insertCartItem(newItem: CartUIState, userId: String)
	suspend fun updateCartItem(item: CartUIState, userId: String)
	suspend fun deleteCartItem(itemId: Int, userId: String)
	suspend fun emptyCartItem(userId: String)


	suspend fun addUser(userData: UserUIState)
	suspend fun deleteUser(userId: String)
	suspend fun clearUser()

	suspend fun placeOrder(newOrder: OrderUIState, userId: String)

	suspend fun setStatusOfOrderByUserId(orderId: String, userId: String, status: String)


	suspend fun getUserById(userId: String): UserUIState?

	suspend fun getEmails(): EmailData? //getting email from firebase

	suspend fun getUserByEmailAndPassword(email: String, password: String): UserUIState?

	suspend fun getUserByEmail(emailAddress: String): UserUIState?

	suspend fun getOrdersByUserId(userId: String): List<OrderUIState>?

	suspend fun getAddressByUserId(userId: String): AddressUIState?

	suspend fun getLikesByUserId(userId: String): List<Int>?//ids of liked items
}