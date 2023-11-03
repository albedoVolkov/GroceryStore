package com.example.grocerystore.data.source.local.user

import android.util.Log
import com.example.grocerystore.data.helpers.EmailData
import com.example.grocerystore.data.helpers.UIstates.item.AddressUIState
import com.example.grocerystore.data.helpers.UIstates.item.CartUIState
import com.example.grocerystore.data.helpers.UIstates.item.OrderUIState
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState
import com.example.grocerystore.data.source.UserDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class UserLocalDataSource internal constructor(
        private val userDao: UserDao,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    ) : UserDataSource {

    companion object{
        private const val TAG = "UserLocalDataSource"
    }

    override suspend fun addUser(userData: UserUIState): Unit = withContext(ioDispatcher) {
        try {
            userDao.insert(userData)
        }catch (e: Exception){
            Log.d(TAG," addUser : error : $e")
        }
    }

    override suspend fun deleteUser(userId: String): Unit = withContext(ioDispatcher) {
        try {
            userDao.delete(userId)
        }catch (e: Exception){
            Log.d(TAG," deleteUser : error : $e")
        }
    }

    override suspend fun clearUser() {
        try{
            userDao.clear()
        }catch (e: Exception){
            Log.d(TAG," clearUser : error : $e")
        }
    }

    override suspend fun updateEmail(email: String, userId: String): Unit = withContext(ioDispatcher) {
        try {
            val oldUser = getUserById(userId) ?: throw Exception("User not found")

            userDao.update(oldUser.copy(email = email))
        }catch (e: Exception){
            Log.d(TAG," updateEmail : error : $e")
        }
    }

    override suspend fun getEmails(): EmailData? {//EmailData
        return EmailData(arrayListOf<String>())
    }

    override suspend fun likeProduct(productId: Int, userId: String): Unit = withContext(ioDispatcher) {
        try {
            val oldUser = getUserById(userId) ?: throw Exception("User not found")

            val newListLikes = oldUser.likes.toMutableList()
            newListLikes.add(productId)

            userDao.update(oldUser.copy(likes = newListLikes))
        }catch (e: Exception){
            Log.d(TAG," likeProduct : error : $e")
        }
    }

    override suspend fun dislikeProduct(productId: Int, userId: String): Unit = withContext(ioDispatcher) {
        try{
            val oldUser = getUserById(userId) ?: throw Exception("User not found")

            val newListLikes = oldUser.likes.toMutableList()
            newListLikes.remove(productId)

            userDao.update(oldUser.copy(likes = newListLikes))
        }catch (e: Exception){
            Log.d(TAG," dislikeProduct : error : $e")
        }
    }

    override suspend fun updateAddress(newAddress: AddressUIState, userId: String): Unit = withContext(ioDispatcher) {
        try{
            val oldUser = getUserById(userId) ?: throw Exception("User not found")

            userDao.update(oldUser.copy(address = newAddress))
        }catch (e: Exception){
            Log.d(TAG," updateAddress : error : $e")
        }
    }

    override suspend fun insertCartItem(newItem: CartUIState, userId: String): Unit = withContext(ioDispatcher) {
        try{
            val oldUser = getUserById(userId) ?: throw Exception("User not found")

            val cart = oldUser.cart.toMutableList()
            cart.add(newItem)
            userDao.update(oldUser.copy(cart = cart))
        }catch (e: Exception){
            Log.d(TAG," insertCartItem : error : $e")
        }
    }

    override suspend fun updateCartItem(item: CartUIState, userId: String): Unit = withContext(ioDispatcher) {
        try{
            val oldUser = getUserById(userId) ?: throw Exception("User not found")

            val cart = oldUser.cart.toMutableList()
            for(elem in cart){
                if(elem.itemData.id == item.itemData.id){
                    cart.remove(elem)
                    cart.add(item)
                    break
                }
            }
            userDao.update(oldUser.copy(cart = cart))
        }catch (e: Exception){
            Log.d(TAG," updateCartItem : error : $e")
        }
    }

    override suspend fun deleteCartItem(itemId: Int, userId: String): Unit = withContext(ioDispatcher) {
        try{
            val oldUser = getUserById(userId) ?: throw Exception("User not found")

            val cart = oldUser.cart.toMutableList()
            for(elem in cart){
                if(elem.itemData.id == itemId){
                    cart.remove(elem)
                    break
                }
            }
            userDao.update(oldUser.copy(cart = cart))
        }catch (e: Exception){
            Log.d(TAG," deleteCartItem : error : $e")
        }
    }

    override suspend fun emptyCartItem(userId: String): Unit = withContext(ioDispatcher) {
        try{
            val oldUser = getUserById(userId) ?: throw Exception("User not found")

            val cart = oldUser.cart.toMutableList()
            cart.clear()
            userDao.update(oldUser.copy(cart = cart))
        }catch (e: Exception){
            Log.d(TAG," emptyCartItem : error : $e")
        }
    }

    override suspend fun placeOrder(newOrder: OrderUIState, userId: String): Unit = withContext(ioDispatcher) {
        // add order to customer and
        // specific items to their owners
        // empty customers cart

        try{
            val oldUser = getUserById(userId) ?: throw Exception("User not found")

            val newListOrders = oldUser.orders.toMutableList()
            newListOrders.add(newOrder)

            userDao.update(oldUser.copy(orders = newListOrders))
        }catch (e: Exception){
            Log.d(TAG," placeOrder : error : $e")
        }
    }

    override suspend fun setStatusOfOrderByUserId(orderId: String, userId: String, status: String): Unit = withContext(ioDispatcher) {
        try{
            val oldUser = getUserById(userId) ?: throw Exception("User not found")

            val orders = oldUser.orders.toMutableList()
            for(elem in orders){
                if(elem.orderId == orderId){
                    orders.remove(elem)
                    orders.add(elem.copy(status = status))
                }
            }
            userDao.update(oldUser.copy(orders = orders))

        }catch (e: Exception){
            Log.d(TAG," setStatusOfOrderByUserId : error : $e")
        }
    }




    override suspend fun getUserById(userId: String): UserUIState? = withContext(ioDispatcher) {
        try{
            return@withContext userDao.getById(userId)
        }catch (e: Exception){
            Log.d(TAG," getUserById : error : $e")
            return@withContext null
        }
    }



    override suspend fun getUserByEmailAndPassword(email: String, password: String): UserUIState?  = withContext(ioDispatcher) {
        try {
            Log.d(TAG," getUserByEmailAndPassword :  password : $password , email : $email ")
            val user = async { getUserByEmail(email)}.await() ?: throw NullPointerException("getUserByEmail = null")

            Log.d(TAG," getUserByEmailAndPassword : password : $password , ${user.password} , email : $email , ${user.email}")

            if(user.password == password){
                Log.d(TAG," getUserByEmailAndPassword : success")
                return@withContext  user
            }else{
                Log.d(TAG," getUserByEmailAndPassword : error")
                throw Exception("email - correct , password - incorrect")
            }
        }catch (e: Exception){
            Log.d(TAG," getUserByEmailAndPassword : error : $e")
            return@withContext  null
        }
    }

    override suspend fun getUserByEmail(emailAddress: String): UserUIState? = withContext(ioDispatcher) {
        try{
            val user = userDao.getByEmail(emailAddress)
            Log.d(TAG," getUserByEmail : user : $user")
            return@withContext user
        }catch (e: Exception){
            Log.d(TAG," getUserByEmail : user : $e")
            return@withContext null
        }
    }

    override suspend fun getOrdersByUserId(userId: String): List<OrderUIState>? = withContext(ioDispatcher) {
        try{
            return@withContext getUserById(userId)?.orders
        }catch (e: Exception){
            Log.d(TAG," getOrdersByUserId : error : $e")
            return@withContext null
        }
    }

    override suspend fun getAddressByUserId(userId: String): AddressUIState? = withContext(ioDispatcher) {
        try{
            return@withContext getUserById(userId)?.address
        }catch (e: Exception){
            Log.d(TAG," getAddressByUserId : error : $e")
            return@withContext null
        }
    }

    override suspend fun getLikesByUserId(userId: String): List<Int>? = withContext(ioDispatcher) {
        try{
            return@withContext getUserById(userId)?.likes
        }catch (e: Exception){
            Log.d(TAG," getLikesByUserId : error : $e")
            return@withContext null
        }
    }



}