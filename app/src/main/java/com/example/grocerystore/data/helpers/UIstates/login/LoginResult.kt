package com.example.grocerystore.data.helpers.UIstates.login

import com.example.grocerystore.data.helpers.UIstates.user.UserUIStateShort

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
    val success: UserUIStateShort? = null,
    val error: Int? = null,
)