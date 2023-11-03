package com.example.grocerystore.data.repository.user

import com.example.grocerystore.data.helpers.Result
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState
import com.example.grocerystore.data.helpers.UIstates.user.UserUIStateShort

interface UserRepoInterface {

    suspend fun createUser(name: String, email: String, password: String): Result<UserUIState?>

    suspend fun getUserById(id: String,hard: Boolean = true): Result<UserUIState?>

    suspend fun refreshData() : Result<Boolean?>
    suspend fun hardRefreshData(): Result<Boolean?>

    suspend fun login(userData:UserUIState, rememberMe: Boolean): Result<Boolean?>
    //suspend fun signInWithEmailAuthCredential(credential: EmailAuthCredential, isUserLoggedIn: MutableLiveData<Boolean>, context: Context,)
    suspend fun singUp(userData: UserUIState, rememberMe: Boolean) : Result<Boolean?>

    suspend fun addUserLocalSource(userData: UserUIState,) : Result<Boolean?>
    suspend fun deleteUserLocalSource(userId: String,): Result<Boolean?>

    suspend fun singOut(userId: String) : Result<Boolean?>

    suspend fun checkLogin(email: String, password: String,hard : Boolean): Result<UserUIState?>

    suspend fun sendEmailCodeForReturnPassword(email : String) : Result<Boolean?>
    suspend fun insertEmailCodeForReturnPassword(code : String) : Result<Boolean?>

    fun isRememberMeOn(): Result<Boolean?>
}