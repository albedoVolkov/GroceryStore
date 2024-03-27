package com.example.grocerystore.domain.models.login

class SighUpFormState (
    var nameError: Int? = null,
    var emailError: Int? = null,
    var passwordError: Int? = null,
    var secondPasswordError: Int? = null,
    var isDataValid: Boolean = false
)