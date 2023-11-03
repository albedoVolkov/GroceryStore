package com.example.grocerystore

import android.content.Context
import android.content.SharedPreferences
import com.example.grocerystore.data.helpers.UIstates.user.UserUIState
import com.example.grocerystore.data.helpers.UIstates.user.UserUIStateShort
import com.example.grocerystore.data.helpers.UIstates.user.fromStringToUserItem

class ShoppingAppSessionManager(context: Context) {
// ShoppingAppSessionManager is needed for getting just little data of user
    private var userSectionSharedPreferences: SharedPreferences = context.getSharedPreferences(USER_SESSION_DATA, Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = userSectionSharedPreferences.edit()


    fun createLoginSession(user: UserUIState, isRememberMe :Boolean, isLogin :Boolean) {
        editor.putString(KEY_USER, user.toString())
        editor.putBoolean(KEY_REMEMBER_ME, isRememberMe)
        editor.putBoolean(IS_LOGIN, isLogin)

        editor.commit()
    }
    fun deleteLoginSession() {
        editor.clear()
        editor.commit()
    }

    fun getUserShortData(): UserUIStateShort? {
        val userString = userSectionSharedPreferences.getString(KEY_USER, null)
        if (userString != null) {
            val user = fromStringToUserItem(userString)
            return user?.toUserUIStateShort()
        }
        return null
    }

    fun getUserData(): UserUIState? {
        val userString = userSectionSharedPreferences.getString(KEY_USER, null)
        if (userString != null) {
            return fromStringToUserItem(userString)
        }
        return null
    }


    fun isRememberMeOn(): Boolean = userSectionSharedPreferences.getBoolean(KEY_REMEMBER_ME, false)
    fun isLoggedIn(): Boolean = userSectionSharedPreferences.getBoolean(IS_LOGIN, false)


    companion object {
        private const val USER_SESSION_DATA = "userSessionData"

        private const val KEY_USER = "userUIState"
        private const val IS_LOGIN = "isLoggedIn"
        private const val KEY_REMEMBER_ME = "isRemOn"


    }
}
