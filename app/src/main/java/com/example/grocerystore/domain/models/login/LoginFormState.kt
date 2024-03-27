package com.example.grocerystore.domain.models.login

/**
 * Data validation state of the login form.
 */
data class LoginFormState(
    val passwordError: Int? = null,
    val emailError: Int? = null,
    val isDataValid: Boolean = false,
)