package com.truongdinh.drinkorder.ui.screens.profile

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("session_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_USERNAME = "username"

        private const val KEY_CURRENT_TABLE = "current_table"
    }

    fun saveSession(email: String, username: String) {
        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .putString(KEY_USER_EMAIL, email)
            .putString(KEY_USERNAME, username)
            .apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getEmail(): String? = prefs.getString(KEY_USER_EMAIL, null)

    fun getUsername(): String? = prefs.getString(KEY_USERNAME, null)

    fun clearSession() {
        prefs.edit().clear().apply()
    }

    fun saveCurrentTableNumber(tableNumber: Int) {
        prefs.edit()
            .putInt(KEY_CURRENT_TABLE, tableNumber)
            .apply()
    }


    fun getCurrentTableNumber(): Int? {
        val value = prefs.getInt(KEY_CURRENT_TABLE, -1)
        return if (value == -1) null else value
    }

}