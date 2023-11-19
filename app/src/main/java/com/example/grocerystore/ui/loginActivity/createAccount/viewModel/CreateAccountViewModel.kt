package com.example.grocerystore.ui.loginActivity.createAccount.viewModel

import android.util.Log
import android.util.Patterns
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grocerystore.GroceryStoreApplication
import com.example.grocerystore.R
import com.example.grocerystore.data.helpers.UIstates.item.AddressUIState
import com.example.grocerystore.data.helpers.UIstates.login.LoginResult
import com.example.grocerystore.data.helpers.UIstates.login.SighUpFormState
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState
import com.example.grocerystore.data.helpers.Utils
import com.example.grocerystore.data.repository.user.UserRepoInterface
import com.example.grocerystore.services.CreatingNewIdsService
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CreateAccountViewModel(private val userRepository: UserRepoInterface, private val creatingIdsService: CreatingNewIdsService) : ViewModel() {

    companion object {
        const val TAG = "CreateAccountViewModel"
    }


    private val _feedbackResultErrors = MutableLiveData<SighUpFormState>()
    val feedbackResultErrors: LiveData<SighUpFormState> = _feedbackResultErrors

    private val _feedbackResult = MutableLiveData<LoginResult>()
    val feedbackResult: LiveData<LoginResult> = _feedbackResult


    fun signUp(name: String, email: String, password: String): Boolean {
        var success = false

        viewModelScope.launch {
            try {

                //checking email
                val resultCheck = async {
                    userRepository.checkLogin(email, password, false)
                }.await()

                if (resultCheck.isFailure) {

                    val resultId = async {creatingIdsService.createIdForUserUIState()}.await()

                    if (resultId.isSuccess && resultId.getOrNull() != null) {

                    //creating UserUIState
                    val user = UserUIState(resultId.getOrNull().toString(), name, "", email, password, "",listOf(),
                        listOf(), listOf(),
                        listOf(), Utils.UserType.CUSTOMER.name)


                    //sing up user
                    val resultSignUp = async {
                        userRepository.singUp(user, true)
                    }.await()

                        if (resultSignUp.getOrNull() == true) {

                            //return success
                            _feedbackResult.value =
                                LoginResult(success = user.toUserUIStateShort())
                                success = true

                        } else {
                            _feedbackResult.value =
                            LoginResult(error = R.string.create_account_error)
                            success = false
                        }
                    } else {
                        _feedbackResult.value = LoginResult(error = R.string.id_creating_error)
                        success = false
                    }
                } else {
                    _feedbackResult.value = LoginResult(error = R.string.account_already_created)
                    success = false
                }

            } catch (e: Exception) {
                Log.d(TAG, " signUp : error : $e")
                _feedbackResult.value = LoginResult(error = R.string.create_account_error)
                success = false
            }
        }
        return success
    }


    fun dataChanged(name: String, email: String, password: String, secondPassword: String) {
        val result = SighUpFormState()
        if (!isNameValid(name)) {
            result.nameError = R.string.invalid_name
        }
        if (!isEmailValid(email)) {
            result.emailError = R.string.invalid_email
        }
        if (!isPasswordValid(password)) {
            result.passwordError = R.string.invalid_password
        }
        if (!isPasswordsValid(password, secondPassword)) {
            result.secondPasswordError = R.string.invalid_secondPassword
        }

        if (result.passwordError == null && result.emailError == null && result.secondPasswordError == null && result.nameError == null) {
            result.isDataValid = true
        }

        _feedbackResultErrors.value = result
    }


    // A placeholder name validation check
    private fun isNameValid(name: String): Boolean {
        return (name.length > 5) && (!name.isDigitsOnly()) && (!name.contains(":>?{}!@#$%^&*()_+=-~`/|][\\,."))
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


    // A placeholder secondPassword validation check
    private fun isPasswordsValid(password: String, secondPassword: String): Boolean {
        return password == secondPassword
    }
}