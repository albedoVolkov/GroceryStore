package com.example.grocerystore.domain.services

import android.content.Context
import android.content.SharedPreferences
import com.example.grocerystore.domain.utils.ConstantsDomain

class SessionManager(context: Context) {
    // ShoppingAppSessionManager is needed for getting current user
    private var userSectionSharedPreferences: SharedPreferences = context.getSharedPreferences(
        ConstantsDomain.USER_SESSION_DATA, Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = userSectionSharedPreferences.edit()


    fun updateDataLoginSession(userId : String = getUserId(),isBlackMode: Boolean = isBlackMode(), isLogin :Boolean = isLoggedIn(), isRememberMe :Boolean = isRememberMeOn()) {
        editor.putString(ConstantsDomain.USER_ID, userId)
        editor.putBoolean(ConstantsDomain.IS_BLACK_MODE, isBlackMode)
        editor.putBoolean(ConstantsDomain.IS_LOGIN, isLogin)
        editor.putBoolean(ConstantsDomain.IS_REMEMBER_ME, isRememberMe)
        editor.commit()
    }
    fun deleteLoginSession() {
        editor.clear()
        editor.commit()
    }


    fun isRememberMeOn(): Boolean = userSectionSharedPreferences.getBoolean(ConstantsDomain.IS_REMEMBER_ME, false)
    fun isLoggedIn(): Boolean = userSectionSharedPreferences.getBoolean(ConstantsDomain.IS_LOGIN, false)
    fun isBlackMode(): Boolean = userSectionSharedPreferences.getBoolean(ConstantsDomain.IS_BLACK_MODE, false)
    fun getUserId(): String = userSectionSharedPreferences.getString(ConstantsDomain.USER_ID, "") ?: ""
}
