package com.example.grocerystore.ui.loginActivity.fragments.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerystore.R
import com.example.grocerystore.data.helpers.UIstates.login.LoginFormState
import com.example.grocerystore.data.helpers.UIstates.login.LoginResult
import com.example.grocerystore.data.repository.user.UserRepository
import com.example.grocerystore.locateLazy
import com.example.grocerystore.services.SessionManager
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class LoginFragmentViewModel : ViewModel(){

    private val TAG = "LoginFragmentViewModel"

    private val userRepository by locateLazy<UserRepository>()
    private val sessionManager by locateLazy<SessionManager>()


    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult


    fun getLoginStatus() {
        val result = sessionManager.isLoggedIn()
        _isLoggedIn.value = result
    }



    fun login(email: String, password: String) : Boolean{
        // can be launched in a separate asynchronous job
        var success = false
        viewModelScope.launch {
            try {
                val userNotClear =
                    async { userRepository.checkLogin(email, password, false) }.await()
                //getting status from userNotClear
                if (userNotClear.isFailure) {
                    throw Exception("Result is ERROR")
                }

                //getting value from userNotClear
                val user = userNotClear.getOrNull()
                    ?: throw Exception("user not found")

                //logging user
                async { userRepository.login(user.userId, true) }.await()

                //return success
                _loginResult.value = LoginResult(success = user.toUserUIStateShort())
                success = true
            } catch (e: Exception) {
                Log.d(TAG, " login : error : $e")
                _loginResult.value = LoginResult(error = -1)
                success = false
            }
        }

        return success
    }


    fun loginDataChanged(email: String, password: String) {
        if (isEmailValid(email)) {
            if (isPasswordValid(password)) {
                _loginForm.value = LoginFormState(isDataValid = true)
            }else {
                _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
            }
        } else {
            _loginForm.value = LoginFormState(emailError = R.string.invalid_email)
        }
    }


    // A placeholder email validation check
    private fun isEmailValid(email: String): Boolean {
        return if (email.contains("@") && email.contains(".")) {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
        } else {
            false
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 3
    }
}