package com.example.grocerystore.data.source.local.user

import android.util.Log
import com.example.grocerystore.domain.models.user.UserUIState
import com.example.grocerystore.data.source.UserDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
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
            userDao.deleteById(userId)
            return@withContext Result.success(true)
        }catch (e: Exception){
            Log.d(TAG," deleteUser : error : $e")
            return@withContext Result.failure(e)
        }
    }

    override suspend fun updateUser(newUser: UserUIState): Result<Boolean> = withContext(ioDispatcher) {
        try {
            userDao.deleteById(newUser.userId)
            userDao.insert(newUser)
            return@withContext Result.success(true)

        }catch (e: Exception){
            Log.d(TAG," updateUser : error : $e")
            return@withContext Result.failure(e)
        }
    }


    override suspend fun clearUser() : Result<Boolean> = withContext(ioDispatcher) {
        try{
            userDao.clear()
            Log.d(TAG,"clearUser : result : true")
            return@withContext Result.success(true)
        }catch (e: Exception){
            Log.d(TAG,"clearUser : error : $e")
            return@withContext Result.failure(e)
        }
    }





//    override suspend fun placeOrder(newOrder: OrderUIState, userId: String): Result<Boolean> = withContext(ioDispatcher) {
//        // add order to customer and
//        // specific items to their owners
//        // empty customers cart
//
//        try{
//
//            val oldUser = getUserById(userId)
//
//            if(oldUser.isSuccess && oldUser.getOrNull() != null) {
//
//                val newListOrders = oldUser.getOrNull()!!.orders.toMutableList()
//                newListOrders.add(newOrder)
//
//                userDao.deleteById(oldUser.getOrNull()!!.userId)
//                userDao.insert(oldUser.getOrNull()!!.copy(orders = newListOrders))
//
//                return@withContext Result.success(true)
//            }else{
//                return@withContext Result.success(false)
//            }
//
//        }catch (e: Exception){
//            Log.d(TAG," placeOrder : error : $e")
//            return@withContext Result.failure(e)
//        }
//    }


//    override suspend fun setStatusOfOrderByUserId(orderId: String, userId: String, status: String): Result<Boolean> = withContext(ioDispatcher) {
//        try{
//            val oldUser = getUserById(userId)
//
//            if(oldUser.isSuccess && oldUser.getOrNull() != null) {
//
//                val orders = oldUser.getOrNull()!!.orders.toMutableList()
//                for (elem in orders) {
//                    if (elem.orderId == orderId) {
//                        orders.remove(elem)
//                        orders.add(elem.copy(status = status))
//                    }
//                }
//
//                userDao.deleteById(oldUser.getOrNull()!!.userId)
//                userDao.insert(oldUser.getOrNull()!!.copy(orders = orders))
//
//                return@withContext Result.success(true)
//            }else{
//                return@withContext Result.success(false)
//            }
//
//        }catch (e: Exception){
//            Log.d(TAG," setStatusOfOrderByUserId : error : $e")
//            return@withContext Result.failure(e)
//        }
//    }


    override suspend fun getUserById(userId: String): Result<UserUIState?> = withContext(ioDispatcher) {
        try{
            val newUser = userDao.getById(userId)
            Log.d(TAG,"getUserById : user : $newUser")
            return@withContext Result.success(newUser)
        }catch (e: Exception){
            Log.d(TAG,"getUserById : user : $e")
            return@withContext Result.failure(e)
        }
    }

    override fun getUserByIdFlow(userId: String): Flow<UserUIState?> = userDao.getByIdFlow(userId)


    override suspend fun getUserByEmail(emailAddress: String): Result<UserUIState?> = withContext(ioDispatcher) {
        try {
            Log.d(TAG,"getUserByEmail : emailAddress : $emailAddress")
            val user = userDao.getByEmail(emailAddress)
            Log.d(TAG,"    getUserByEmail : user : $user")
            return@withContext Result.success(user)
        }catch (e: Exception){
            Log.d(TAG,"getUserByEmail : error : $e")
            return@withContext  Result.failure(e)
        }
    }


    override suspend fun getAllUsers(): Result<List<UserUIState>> = withContext(ioDispatcher) {
        try {
            val userList = userDao.getListOfUsers()
            Log.d(TAG,"getAllUsers : list : $userList")
            return@withContext Result.success(userList)
        }catch (e: Exception){
            Log.d(TAG,"getAllUsers : error : $e")
            return@withContext  Result.failure(e)
        }
    }

    override fun getAllUsersFlow(): Flow<List<UserUIState>> = userDao.getListOfUsersFlow()



}