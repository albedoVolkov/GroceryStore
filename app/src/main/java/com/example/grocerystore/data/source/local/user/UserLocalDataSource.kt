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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class UserLocalDataSource internal constructor(
        private val userDao: UserDao,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    ) : UserDataSource {

    private val TAG = "UserLocalDataSource"


    override suspend fun addUser(userData: UserUIState): Result<Boolean> = withContext(ioDispatcher) {
        try {
            userDao.insert(userData)
            return@withContext Result.success(true)
        }catch (e: Exception){
            Log.d(TAG," addUser : error : $e")
            return@withContext Result.failure(e)
        }
    }

    override suspend fun deleteUser(userId: String): Result<Boolean> = withContext(ioDispatcher) {
        try {
            val oldUser = getUserById(userId)

            if(oldUser.isSuccess && oldUser.getOrNull() != null) {
                userDao.delete(oldUser.getOrNull()!!)
                return@withContext Result.success(true)
            }else{
                return@withContext Result.success(false)
            }
        }catch (e: Exception){
            Log.d(TAG," deleteUser : error : $e")
            return@withContext Result.failure(e)
        }
    }

    override suspend fun clearUser() : Result<Boolean> = withContext(ioDispatcher) {
        try{
            userDao.clear()
            return@withContext Result.success(true)
        }catch (e: Exception){
            Log.d(TAG," clearUser : error : $e")
            return@withContext Result.failure(e)
        }
    }









    override suspend fun updateEmail(email: String, userId: String): Result<Boolean> = withContext(ioDispatcher) {
        try {
            val oldUser = getUserById(userId)

            if(oldUser.isSuccess && oldUser.getOrNull() != null) {

                userDao.update(oldUser.getOrNull()!!.copy(email = email))

                return@withContext Result.success(true)
            }else{
                return@withContext Result.success(false)
            }
        }catch (e: Exception){
            Log.d(TAG," updateEmail : error : $e")
            return@withContext Result.failure(e)
        }
    }

    override suspend fun getEmails(): Result<EmailData?> = withContext(ioDispatcher) {
        try {
            return@withContext Result.success(EmailData(arrayListOf<String>()))
        }catch (e: Exception){
            Log.d(TAG," getEmails : error : $e")
            return@withContext Result.failure(e)
        }
    }








    override suspend fun likeProduct(productId: String, userId: String): Result<Boolean> = withContext(ioDispatcher) {
        try {
            val oldUser = getUserById(userId) ?: throw Exception("User not found")

            if(oldUser.isSuccess && oldUser.getOrNull() != null) {
                val newListLikes = oldUser.getOrNull()!!.likes.toMutableList()
                newListLikes.add(productId)

                userDao.update(oldUser.getOrNull()!!.copy(likes = newListLikes))

                return@withContext Result.success(true)
            }else{
                return@withContext Result.success(false)
            }
        }catch (e: Exception){
            Log.d(TAG," likeProduct : error : $e")
            return@withContext Result.failure(e)
        }
    }

    override suspend fun dislikeProduct(productId: String, userId: String): Result<Boolean> = withContext(ioDispatcher) {
        try{
            val oldUser = getUserById(userId)

            if(oldUser.isSuccess && oldUser.getOrNull() != null) {
                val newListLikes = oldUser.getOrNull()!!.likes.toMutableList()
                newListLikes.remove(productId)

                userDao.update(oldUser.getOrNull()!!.copy(likes = newListLikes))

                return@withContext Result.success(true)
            }else{
                return@withContext Result.success(false)
            }

        }catch (e: Exception){
            Log.d(TAG," dislikeProduct : error : $e")
            return@withContext Result.failure(e)
        }
    }

    override suspend fun updateAddress(newAddress: List<AddressUIState>, userId: String): Result<Boolean> = withContext(ioDispatcher) {
        try{
            val oldUser = getUserById(userId)

            if(oldUser.isSuccess && oldUser.getOrNull() != null) {
                userDao.update(oldUser.getOrNull()!!.copy(address = newAddress))

                return@withContext Result.success(true)
            }else{
                return@withContext Result.success(false)
            }
        }catch (e: Exception){
            Log.d(TAG," updateAddress : error : $e")
            return@withContext Result.failure(e)
        }
    }








    override suspend fun insertCartItem(newItem: CartUIState, userId: String): Result<Boolean> = withContext(ioDispatcher) {
        try{

            val oldUser = getUserById(userId).getOrNull()  ?: throw Exception("user is not found")

            val cart = oldUser.cart.toMutableList()
            cart.add(newItem)

            userDao.update(oldUser.copy(cart = cart))

            return@withContext Result.success(true)

        }catch (e: Exception){
            Log.d(TAG," insertCartItem : error : $e")
            return@withContext Result.failure(e)
        }
    }

    override suspend fun updateCartItem(item: CartUIState, userId: String): Result<Boolean> = withContext(ioDispatcher) {
        try{

            val oldUser = getUserById(userId)

            if(oldUser.isSuccess && oldUser.getOrNull() != null) {

                val cart = oldUser.getOrNull()!!.cart.toMutableList()
                for(elem in cart){
                    if(elem.itemData.id == item.itemData.id){
                        cart.remove(elem)
                        cart.add(item)
                        break
                    }
                }
                userDao.update(oldUser.getOrNull()!!.copy(cart = cart))

                return@withContext Result.success(true)
            }else{
                return@withContext Result.success(false)
            }
        }catch (e: Exception){
            Log.d(TAG," updateCartItem : error : $e")
            return@withContext Result.failure(e)
        }
    }

    override suspend fun deleteCartItem(itemId: String, userId: String): Result<Boolean> = withContext(ioDispatcher) {
        try{
            val oldUser = getUserById(userId)

            if(oldUser.isSuccess && oldUser.getOrNull() != null) {

                val cart = oldUser.getOrNull()!!.cart.toMutableList()
                for(elem in cart){
                    if(elem.cartId == itemId){
                        cart.remove(elem)
                        break
                    }
                }
                userDao.update(oldUser.getOrNull()!!.copy(cart = cart))

                return@withContext Result.success(true)
            }else{
                return@withContext Result.success(false)
            }
        }catch (e: Exception){
            Log.d(TAG," deleteCartItem : error : $e")
            return@withContext Result.failure(e)
        }
    }

    override suspend fun emptyCartItem(userId: String): Result<Boolean> = withContext(ioDispatcher) {
        try{
            val oldUser = getUserById(userId)

            if(oldUser.isSuccess && oldUser.getOrNull() != null) {

                userDao.update(oldUser.getOrNull()!!.copy(cart = listOf()))

                return@withContext Result.success(true)
            }else{
                return@withContext Result.success(false)
            }

        }catch (e: Exception){
            Log.d(TAG," emptyCartItem : error : $e")
            return@withContext Result.failure(e)
        }
    }










    override suspend fun placeOrder(newOrder: OrderUIState, userId: String): Result<Boolean> = withContext(ioDispatcher) {
        // add order to customer and
        // specific items to their owners
        // empty customers cart

        try{

            val oldUser = getUserById(userId)

            if(oldUser.isSuccess && oldUser.getOrNull() != null) {

                val newListOrders = oldUser.getOrNull()!!.orders.toMutableList()
                newListOrders.add(newOrder)

                userDao.update(oldUser.getOrNull()!!.copy(orders = newListOrders))

                return@withContext Result.success(true)
            }else{
                return@withContext Result.success(false)
            }

        }catch (e: Exception){
            Log.d(TAG," placeOrder : error : $e")
            return@withContext Result.failure(e)
        }
    }

    override suspend fun setStatusOfOrderByUserId(orderId: String, userId: String, status: String): Result<Boolean> = withContext(ioDispatcher) {
        try{
            val oldUser = getUserById(userId)

            if(oldUser.isSuccess && oldUser.getOrNull() != null) {

                val orders = oldUser.getOrNull()!!.orders.toMutableList()
                for (elem in orders) {
                    if (elem.orderId == orderId) {
                        orders.remove(elem)
                        orders.add(elem.copy(status = status))
                    }
                }

                userDao.update(oldUser.getOrNull()!!.copy(orders = orders))

                return@withContext Result.success(true)
            }else{
                return@withContext Result.success(false)
            }

        }catch (e: Exception){
            Log.d(TAG," setStatusOfOrderByUserId : error : $e")
            return@withContext Result.failure(e)
        }
    }











    override suspend fun getUserCartsById(userId: String): Result<List<CartUIState>> = withContext(ioDispatcher) {
        try{
            val newUser = userDao.getById(userId)
            if(newUser != null) {
                Log.d(TAG, "getUserCartsById : user : $newUser")
                return@withContext Result.success(newUser.cart)
            }else{
                return@withContext Result.failure(Exception("user is not found"))
            }
        }catch (e: Exception){
            Log.d(TAG," getUserCartsById : user : $e")
            return@withContext Result.failure(e)
        }
    }

    override fun getUserCartsByIdFlow(userId: String): Flow<List<CartUIState>> = userDao.getUserCartsByIdFlow(userId)




    override suspend fun getUserById(userId: String): Result<UserUIState?> = withContext(ioDispatcher) {
        try{
            val newUser = userDao.getById(userId)
            Log.d(TAG,"getUserById : user : $newUser")
            return@withContext Result.success(newUser)
        }catch (e: Exception){
            Log.d(TAG," getUserById : user : $e")
            return@withContext Result.failure(e)
        }
    }

    override fun getUserByIdFlow(userId: String): Flow<UserUIState?> = userDao.getByIdFlow(userId)



    override suspend fun getUserByEmailAndPassword(email: String, password: String): Result<UserUIState?>  = withContext(ioDispatcher) {
        try {

            val user = async { getUserByEmail(email)}.await()

            if(user.isSuccess && user.getOrNull() != null) {
                Log.d(TAG, " getUserByEmailAndPassword : password : $password , ${user.getOrNull()!!.password} , email : $email , ${user.getOrNull()!!.email}")
                if (user.getOrNull()!!.password == password) {
                    return@withContext Result.success(user.getOrNull()!!)
                }
            }
            return@withContext Result.success(null)
        }catch (e: Exception){
            Log.d(TAG," getUserByEmailAndPassword : error : $e")
            return@withContext  Result.failure(e)
        }
    }

    override suspend fun getUserByEmail(emailAddress: String): Result<UserUIState?> = withContext(ioDispatcher) {
        try {
            val user = userDao.getByEmail(emailAddress)
            Log.d(TAG," getUserByEmail : user : $user")
            return@withContext Result.success(user)
        }catch (e: Exception){
            Log.d(TAG," getUserByEmail : error : $e")
            return@withContext  Result.failure(e)
        }
    }



    override suspend fun getAllUsers(): Result<List<UserUIState>> = withContext(ioDispatcher) {
        try {
            val userList = userDao.getListOfUsers()
            Log.d(TAG," getAllUsers : userList : ${userList}")
            return@withContext Result.success(userList)
        }catch (e: Exception){
            Log.d(TAG," getAllUsers : error : $e")
            return@withContext  Result.failure(e)
        }
    }

    override fun getAllUsersFlow(): Flow<List<UserUIState>> = userDao.getListOfUsersFlow()



}