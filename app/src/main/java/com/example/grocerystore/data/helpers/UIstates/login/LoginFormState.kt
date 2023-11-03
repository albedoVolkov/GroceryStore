package com.example.grocerystore.data.helpers.UIstates.login

/**
 * Data validation state of the login form.
 */
data class LoginFormState(
    val passwordError: Int? = null,
    val emailError: Int? = null,
    val isDataValid: Boolean = false,
)