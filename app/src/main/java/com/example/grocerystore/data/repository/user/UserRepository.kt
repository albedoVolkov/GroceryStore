package com.example.grocerystore.data.repository.user

import android.util.Log
import com.example.grocerystore.services.ShoppingAppSessionManager
import com.example.grocerystore.data.helpers.UIstates.item.CartUIState
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState
import com.example.grocerystore.data.source.local.user.UserLocalDataSource
import com.example.grocerystore.data.source.remove.firebase.UserRemoteDataSource

class UserRepository(
    private val remoteSource: UserRemoteDataSource,
    private val localSource: UserLocalDataSource,
    private val sessionManager: ShoppingAppSessionManager,
) : UserRepoInterface {

    companion object {
        const val TAG = "UserRepository"
    }

//    private fun usersCollectionRef() = firebaseDb.collection(USERS_COLLECTION)
//    private fun emailsCollectionRef() = firebaseDb.collection(USERS_COLLECTION).document(EMAIL_MOBILE_DOC)

    override suspend fun getCurrentUser() : Result<UserUIState?> {
        return try {
            //getting from local data source
            val user = localSource.getUserById(sessionManager.getUserId())
            Log.d(TAG, " getCurrentUser : user = $user")
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUserById(id: String,hard: Boolean): Result<UserUIState?> {
        return try {
            if(hard) {
                //getting from local data source
                val userFromLocalDB = localSource.getUserById(id)
                Result.success(userFromLocalDB)
            }else{
                //getting from remote data source
                TODO()
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    override suspend fun hardRefreshData(): Result<Boolean?> {
        return try {
            sessionManager.deleteLoginSession()
            localSource.clearUser()
            Result.success(true)
        }catch (e : Exception){
            Result.failure(e)
        }
    }

    override suspend fun refreshData(): Result<Boolean?> {
        return try {
            sessionManager.deleteLoginSession()
            Result.success(true)
        }catch (e : Exception){
            Result.failure(e)
        }
    }



    override suspend fun checkLogin(email: String, password: String, hard : Boolean): Result<UserUIState?> {
        if(!hard){
            //checking in local database
            val oldUser = localSource.getUserByEmailAndPassword(email,password)
            Log.d(TAG, " checkLogin : oldUser = $oldUser")

            return if(oldUser != null){
                Result.success(oldUser)
            }else{
                Result.failure(Exception("such user is not found in local dataBase"))
            }
        }else{
            //checking in remote database
            Log.d(TAG, " checkLogin : checking user in remote source")
            TODO()
        }
    }



    override suspend fun login(userId: String, rememberMe: Boolean): Result<Boolean?> {
        return try {
            sessionManager.updateDataLoginSession(
                userId,
                isLogin = true,
                isRememberMe = rememberMe
            )
            Result.success(true)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun singUp( userData: UserUIState, rememberMe: Boolean): Result<Boolean?> {
        return try {

            Log.d(TAG, " singUp : user = $userData")
            addUserLocalSource(userData)

            //        Log.d(TAG, " login: Updating userdata on Remote Source")
            //        remoteSource.addUser(userData)
            //        remoteSource.updateEmailsAndMobiles(userData.email, userData.mobile)

            sessionManager.updateDataLoginSession( userData.userId,isLogin =  true, isRememberMe = rememberMe)
            Result.success(true)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun singOut(userId: String): Result<Boolean?> {
        return try {
            Log.d(TAG, " singOut : userId = $userId")
            localSource.deleteUser(userId)

            //        Log.d(TAG, " login: Updating userdata on Remote Source")
            //        remoteSource.deleteUser(userData)
            //        remoteSource.updateEmailsAndMobiles(userData.email, userData.mobile)

            sessionManager.updateDataLoginSession(userId = "-1", isLogin = false)

            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }



    override suspend fun addUserLocalSource(userData : UserUIState): Result<Boolean?>  {
        return try{
            Log.d(TAG, " addUserLocalSource: user = $userData")
            localSource.addUser(userData)
            Log.d(TAG, " addUserLocalSource: add user in Local Source - SUCCESS")
            Result.success(true)
        }catch (e: Exception){
            Log.d(TAG, " addUserLocalSource: add user in Local Source - ERROR")
            Result.failure(e)
        }
    }

    override suspend fun deleteUserLocalSource(userId: String): Result<Boolean?>  {
        return try{
            Log.d(TAG, " deleteUserLocalSource: userId = $userId")
            localSource.deleteUser(userId)
            Log.d(TAG, " deleteUserLocalSource: delete user from Local Source - SUCCESS")
            Result.success(true)
        }catch (e: Exception){
            Log.d(TAG, " deleteUserLocalSource: delete user from Local Source - ERROR")
            Result.failure(e)
        }
    }



    override suspend fun addProductInBasketOfUser(newItem: CartUIState, userId: String): Result<Boolean?>  {
        return try{
            Log.d(TAG, " addProductInBasketOfUser : item = $newItem , userId = $userId")
            localSource.insertCartItem(newItem,userId)
            Log.d(TAG, " addProductInBasketOfUser: add product in basket of user - SUCCESS")
            Result.success(true)
        }catch (e: Exception){
            Log.d(TAG, " deleteUserLocalSource: add product in basket of user - ERROR")
            Result.failure(e)
        }
    }

    override suspend fun deleteProductInBasketOfUser(itemId: String, userId: String): Result<Boolean?>  {
        return try{
            Log.d(TAG, " deleteProductInBasketOfUser : itemId = $itemId , userId = $userId")
            localSource.deleteCartItem(itemId,userId)
            Log.d(TAG, " deleteProductInBasketOfUser: delete product from basket of user - SUCCESS")
            Result.success(true)
        }catch (e: Exception){
            Log.d(TAG, " deleteProductInBasketOfUser: delete product from basket of user - ERROR")
            Result.failure(e)
        }
    }

    override suspend fun updateProductInBasketOfUser(updatedItem: CartUIState, userId: String): Result<Boolean?>{
        return try{
            Log.d(TAG, " updateProductInBasketOfUser : updatedItem = $updatedItem , userId = $userId")
            localSource.updateCartItem(updatedItem,userId)
            Log.d(TAG, " updateProductInBasketOfUser: update product from basket of user  - SUCCESS")
            Result.success(true)
        }catch (e: Exception){
            Log.d(TAG, " updateProductInBasketOfUser: update product from basket of user - ERROR")
            Result.failure(e)
        }
    }

}