package com.example.grocerystore.data.repository.user

import android.util.Log
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState
import com.example.grocerystore.data.source.local.user.UserLocalDataSource
import com.example.grocerystore.data.source.remove.firebase.UserRemoteDataSource
import com.example.grocerystore.services.SessionManager
import kotlinx.coroutines.flow.Flow

class UserRepository(
    private val remoteSource: UserRemoteDataSource,
    private val localSource: UserLocalDataSource,
    private val sessionManager: SessionManager,
) : UserRepoInterface {

    private val TAG = "UserRepository"

//    private fun usersCollectionRef() = firebaseDb.collection(USERS_COLLECTION)
//    private fun emailsCollectionRef() = firebaseDb.collection(USERS_COLLECTION).document(EMAIL_MOBILE_DOC)


    override fun getCurrentUserFlow(): Flow<UserUIState?> {
        //getting from local data source
        return localSource.getUserByIdFlow(sessionManager.getUserId())
    }

    override suspend fun getCurrentUser(): Result<UserUIState?> {
        //getting from local data source
        val user = localSource.getUserById(sessionManager.getUserId())
        Log.d(TAG, " getCurrentUser: user = ${user.getOrNull()}")
        return user
    }


    override suspend fun getUserById(id: String, hard: Boolean): Result<UserUIState?> {
        if (!hard) {
            //getting from local data source
            return localSource.getUserById(id)
        } else {
            //getting from remote data source
            TODO()
        }
    }


    override suspend fun hardRefreshData(): Result<Boolean?> {
        sessionManager.deleteLoginSession()
        return localSource.clearUser()
    }

    override suspend fun refreshData(): Result<Boolean?> {
        sessionManager.deleteLoginSession()
        return Result.success(true)
    }


    override suspend fun checkLogin(email: String, password: String, hard: Boolean): Result<UserUIState?> {
        if (!hard) {
            //checking in local database
            val oldUser = localSource.getUserByEmailAndPassword(email, password)
            Log.d(TAG, " checkLogin: user = $oldUser")
            return oldUser
        } else {
            //checking in remote database
            Log.d(TAG, " checkLogin : checking user in remote source")
            TODO()
        }
    }


    override suspend fun login(userId: String, rememberMe: Boolean): Result<Boolean?> {
        if (userId != ""){
            sessionManager.updateDataLoginSession(
                userId,
                isLogin = true,
                isRememberMe = rememberMe
            )
            return Result.success(true)
        }else{
            return Result.failure(Exception("userId is null"))
        }
    }


    override suspend fun singUp(userData: UserUIState, rememberMe: Boolean): Result<Boolean?> {
        if (userData.userId != ""){

            Log.d(TAG, " singUp : user = $userData")
            addUserLocalSource(userData)

    //        Log.d(TAG, " login: Updating userdata on Remote Source")
    //        remoteSource.addUser(userData)
    //        remoteSource.updateEmailsAndMobiles(userData.email, userData.mobile)

            sessionManager.updateDataLoginSession(
                userData.userId,
                isLogin = true,
                isRememberMe = rememberMe
            )

            return Result.success(true)
        }else{
            return Result.failure(Exception("userId is null"))
        }
    }


    override suspend fun singOut(userId: String): Result<Boolean?> {
        if (userId != ""){
            Log.d(TAG, " singOut : userId = $userId")

            sessionManager.updateDataLoginSession(userId = "-1", isLogin = false)

            return Result.success(true)
        }else{
            return Result.failure(Exception("userId is null"))
        }
    }


    override suspend fun addUserLocalSource(userData: UserUIState): Result<Boolean?> {
        if (userData.userId != ""){
            Log.d(TAG, " addUserLocalSource : user = $userData")
            return localSource.addUser(userData)
        }else{
            return Result.failure(Exception("userId is null"))
        }
    }


    override suspend fun deleteUserLocalSource(userId: String): Result<Boolean?> {
        if (userId != ""){
            val user = getUserById(userId)

            if(!(user.isSuccess && user.getOrNull() != null)){
                return Result.failure(Exception("userId is not found"))
            }

            Log.d(TAG, " deleteUserLocalSource : user = $user")
            return localSource.deleteUser(userId)
        }else{
            return Result.failure(Exception("userId is null"))
        }
    }


}