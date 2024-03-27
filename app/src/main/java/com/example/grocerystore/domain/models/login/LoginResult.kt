package com.example.grocerystore.domain.models.login

import com.example.grocerystore.domain.models.user.UserUIStateShort

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: UserUIStateShort? = null,
    val error: Int? = null,
)