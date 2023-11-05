package com.example.grocerystore.data.repository.user

import android.util.Log
import com.example.grocerystore.ShoppingAppSessionManager
import com.example.grocerystore.data.helpers.Result
import com.example.grocerystore.data.helpers.UIstates.item.AddressUIState
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState
import com.example.grocerystore.data.helpers.Utils
import com.example.grocerystore.data.source.local.user.UserLocalDataSource
import com.example.grocerystore.data.source.remove.firebase.UserRemoteDataSource
import org.checkerframework.checker.units.qual.A

class UserRepository(
    private val remoteSource: UserRemoteDataSource,
    private val localSource: UserLocalDataSource,
    private val sessionManager: ShoppingAppSessionManager,
) : UserRepoInterface {

    val TAG = "UserRepository"

//    private fun usersCollectionRef() = firebaseDb.collection(USERS_COLLECTION)
//
//    private fun emailsCollectionRef() = firebaseDb.collection(USERS_COLLECTION).document(EMAIL_MOBILE_DOC)
    @Volatile
    private var idForNewUser = 0

    // in-memory cache of the loggedInUser object
    private var user: UserUIState? = null
        private set

    private val isLoggedIn: Boolean
        get() = user != null

    init {
        getData()
    }

    override fun getLoggedInStatus(): Result<Boolean?> {
        return try {
            Result.Success(isLoggedIn)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    override fun getData() {
        try {
            val oldUser = sessionManager.getUserData()
            if (oldUser != null) {
                user = oldUser
                Log.d(TAG,"getData : oldUser : $oldUser")
            }else{
                user = null
                Log.d(TAG,"getData : oldUser : null")
            }
        } catch (e: Exception) {
            Log.d(TAG,"getData : error : $e")
        }
    }


    suspend override fun createUser(name: String, email: String, password: String): Result<UserUIState?> {
        try {
            idForNewUser += 1

            user = UserUIState(idForNewUser.toString(),name,"",email,password, listOf(),
                AddressUIState(), listOf(),
                listOf(),Utils.UserType.CUSTOMER.name)

            return Result.Success(user)
        } catch (e: Exception) {
            return Result.Error(e)
        }
    }

    override suspend fun getUserById(id: String,hard: Boolean): Result<UserUIState?> {
        return try {
            if(hard) {
                //getting from local data source
                val userFromLocalDB = localSource.getUserById(id)
                Result.Success(userFromLocalDB)
            }else{
                //getting from remote data source
                TODO()
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun hardRefreshData(): Result<Boolean?> {
        try {
            sessionManager.deleteLoginSession()
            localSource.clearUser()
            return Result.Success(true)
        }catch (e : Exception){
            return Result.Error(e)
        }
    }

    override suspend fun refreshData(): Result<Boolean?> {
        try {
            sessionManager.deleteLoginSession()
            return Result.Success(true)
        }catch (e : Exception){
            return Result.Error(e)
        }
    }

    override suspend fun login(userData:UserUIState, rememberMe: Boolean): Result<Boolean?> {
        return try {
            sessionManager.createLoginSession(
                userData,
                rememberMe,
                true,
            )

            user = userData

            Result.Success(true)
        }catch (e: Exception){
            Result.Error(e)
        }
    }

    override suspend fun checkLogin(email: String, password: String, hard : Boolean): Result<UserUIState?> {
        if(!hard){
            //checking in local database
            Log.d(TAG, " checkLogin : checking user in local source")

            val oldUser = localSource.getUserByEmailAndPassword(email,password)
            return if(oldUser != null){
                Log.d(TAG, " checkLogin : user is found")
                Result.Success(oldUser)
            }else{
                Log.d(TAG, " checkLogin : user is not found")
                Result.Error(Exception("such user is not found in local dataBase"))
            }
        }else{
            //checking in remote database
            Log.d(TAG, " checkLogin : checking user in remote source")

            TODO()
        }
    }

    override suspend fun singUp( userData: UserUIState, rememberMe: Boolean,): Result<Boolean?> {
        try {

            Log.d(TAG, " singUp: add user in local source and remote source")
            addUserLocalSource(userData)

    //        Log.d(TAG, " login: Updating userdata on Remote Source")
    //        remoteSource.addUser(userData)
    //        remoteSource.updateEmailsAndMobiles(userData.email, userData.mobile)


            sessionManager.createLoginSession(userData, rememberMe, true,)

            return Result.Success(true)
        }catch (e: Exception){
            return Result.Error(e)
        }
    }

    override suspend fun singOut(userId: String): Result<Boolean?> {
        try {
            Log.d(TAG, " login: Updating user in Local Source")
            localSource.deleteUser(userId)

            //        Log.d(TAG, " login: Updating userdata on Remote Source")
            //        remoteSource.deleteUser(userData)
            //        remoteSource.updateEmailsAndMobiles(userData.email, userData.mobile)

            sessionManager.deleteLoginSession()

            return Result.Success(true)
        }catch (e: Exception){
            return Result.Error(e)
        }
    }

    override suspend fun sendEmailCodeForReturnPassword(email: String): Result<Boolean?> {
        TODO()
    }

    override suspend fun insertEmailCodeForReturnPassword(code: String): Result<Boolean?> {
        TODO()
    }

    override fun isRememberMeOn(): Result<Boolean?> {
        try {
            if (sessionManager.isRememberMeOn()){
                return Result.Success(true)
            }else{
                return Result.Success(false)
            }
        }catch (e: Exception){
            return Result.Error(e)
        }
    }

    override suspend fun addUserLocalSource(userData : UserUIState): Result<Boolean?>  {
        try{
            localSource.addUser(userData)
            Log.d(TAG, " addUserLocalSource: add user in Local Source - SUCCESS")
            return Result.Success(true)
        }catch (e: Exception){
            Log.d(TAG, " addUserLocalSource: add user in Local Source - ERROR")
            return Result.Error(e)
        }
    }

    override suspend fun deleteUserLocalSource(userId: String): Result<Boolean?>  {
        try{
            localSource.deleteUser(userId)
            Log.d(TAG, " deleteUserLocalSource: delete user from Local Source - SUCCESS")
            return Result.Success(true)
        }catch (e: Exception){
            Log.d(TAG, " deleteUserLocalSource: delete user from Local Source - ERROR")
            return Result.Error(e)
        }
    }





}