package com.example.grocerystore.data.source.remove.firebase

import com.example.grocerystore.data.helpers.EmailData
import com.example.grocerystore.data.helpers.UIstates.item.AddressUIState
import com.example.grocerystore.data.helpers.UIstates.item.CartUIState
import com.example.grocerystore.data.helpers.UIstates.item.OrderUIState
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState
import com.example.grocerystore.data.source.UserDataSource

class UserRemoteDataSource : UserDataSource{


    companion object{
        const val USERS_COLLECTION = "users collection"
        const val EMAIL_DOC = "email document"
        const val TAG = "UserRemoteDataSource"
        const val ID_DOC = "id document"
        const val USERS_LIKES = "users likes document"
    }

//    private val firebaseDb: FirebaseFireStore = Firebase.fireStore
//
//    private fun usersCollectionRef() = firebaseDb.collection(USERS_COLLECTION)
//    private fun allEmailsRef() = firebaseDb.collection(USERS_COLLECTION).document(EMAIL_DOC)
//
//

    override suspend fun updateEmail(email: String, userId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updateAddress(newAddress: List<AddressUIState>, userId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun likeProduct(productId: String, userId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun dislikeProduct(productId: String, userId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun insertCartItem(newItem: CartUIState, userId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updateCartItem(item: CartUIState, userId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCartItem(itemId: String, userId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun emptyCartItem(userId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun addUser(userData: UserUIState) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUser(userId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun clearUser() {
        TODO("Not yet implemented")
    }

    override suspend fun placeOrder(newOrder: OrderUIState, userId: String) {
        TODO("Not yet implemented")
    }

    override suspend fun setStatusOfOrderByUserId(orderId: String, userId: String, status: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getUserById(userId: String): UserUIState? {
        TODO("Not yet implemented")
    }

    override suspend fun getEmails(): EmailData? {
        TODO("Not yet implemented")
    }

    override suspend fun getUserByEmailAndPassword(email: String, password: String): UserUIState? {
        TODO("Not yet implemented")
    }

    override suspend fun getUserByEmail(emailAddress: String): UserUIState? {
        TODO("Not yet implemented")
    }

    override suspend fun getOrdersByUserId(userId: String): List<OrderUIState>? {
        TODO("Not yet implemented")
    }

    override suspend fun getAddressByUserId(userId: String): List<AddressUIState>? {
        TODO("Not yet implemented")
    }

    override suspend fun getLikesByUserId(userId: String): List<String>? {
        TODO("Not yet implemented")
    }




//    override suspend fun addUser(userData: UserUIState) {
//        usersCollectionRef().add(userData.toHashMap())
//            .addOnSuccessListener {
//                Log.d(TAG, "Doc added")
//            }
//            .addOnFailureListener { e ->
//                Log.d(TAG, "fireStore error occurred: $e")
//            }
//    }
//
//    override suspend fun getUserById(userId: String): UserUIState? {
//        val resRef = usersCollectionRef().whereEqualTo(ID_DOC, userId).get().await()
//        return if (!resRef.isEmpty) {
//            resRef.toObjects(UserUIState::class.java)[0]
//        } else {
//            null
//        }
//    }
//
//    override suspend fun updateEmail(email: String, userId: String) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun getEmails(): EmailData? {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun getUserByEmailAndPassword(email: String, password: String): UserUIState? {
//        val resRef = usersCollectionRef().whereEqualTo(ID_DOC, userId).get().await()
//        return if (!resRef.isEmpty) {
//            resRef.toObjects(UserUIState::class.java)[0]
//        } else {
//            null
//        }
//    }
//
//    override suspend fun likeProduct(productId: Int, userId: String) {
//        val userRef = usersCollectionRef().whereEqualTo(ID_DOC, userId).get().await()
//        if (!userRef.isEmpty) {
//            val docId = userRef.documents[0].id
//            usersCollectionRef().document(docId)
//                .update(USERS_LIKES, FieldValue.arrayUnion(productId))
//        }
//    }
//
//    override suspend fun dislikeProduct(productId: Int, userId: String) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun updateAddress(newAddress: UserUIState.Address, userId: String) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun insertCartItem(newItem: UserUIState.CartItem, userId: String) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun updateCartItem(item: UserUIState.CartItem, userId: String) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun deleteCartItem(itemId: Int, userId: String) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun placeOrder(newOrder: UserUIState.OrderItem, userId: String) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun setStatusOfOrderByUserId(orderId: String, userId: String, status: String) {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun clearUser() {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun getUserByEmail(emailAddress: String): UserUIState? {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun getOrdersByUserId(userId: String): List<UserUIState.OrderItem>? {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun getAddressByUserId(userId: String): UserUIState.Address? {
//        TODO("Not yet implemented")
//    }
//
//    override suspend fun getLikesByUserId(userId: String): List<Int>? {
//        TODO("Not yet implemented")
//    }
}