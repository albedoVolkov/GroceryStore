package com.example.grocerystore.services

import android.content.Context
import android.content.SharedPreferences
import com.example.grocerystore.services.ConstantsSource.IS_LOGIN
import com.example.grocerystore.services.ConstantsSource.USER_SESSION_DATA
import com.example.grocerystore.services.ConstantsSource.IS_BLACK_MODE
import com.example.grocerystore.services.ConstantsSource.IS_REMEMBER_ME
import com.example.grocerystore.services.ConstantsSource.USER_ID

class ShoppingAppSessionManager(context: Context) {
    // ShoppingAppSessionManager is needed for getting current user
    private var userSectionSharedPreferences: SharedPreferences = context.getSharedPreferences(USER_SESSION_DATA, Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = userSectionSharedPreferences.edit()


    fun updateDataLoginSession(userId : String = getUserId(),isBlackMode: Boolean = isBlackMode(), isLogin :Boolean = isLoggedIn(), isRememberMe :Boolean = isRememberMeOn()) {
        editor.putString(USER_ID, userId)
        editor.putBoolean(IS_BLACK_MODE, isBlackMode)
        editor.putBoolean(IS_LOGIN, isLogin)
        editor.putBoolean(IS_REMEMBER_ME, isRememberMe)
        editor.commit()
    }
    fun deleteLoginSession() {
        editor.clear()
        editor.commit()
    }


    fun isRememberMeOn(): Boolean = userSectionSharedPreferences.getBoolean(IS_REMEMBER_ME, false)
    fun isLoggedIn(): Boolean = userSectionSharedPreferences.getBoolean(IS_LOGIN, false)
    fun isBlackMode(): Boolean = userSectionSharedPreferences.getBoolean(IS_BLACK_MODE, false)
    fun getUserId(): String = userSectionSharedPreferences.getString(USER_ID, "") ?: ""
}
