package com.truongdinh.drinkorder.data.local

import android.annotation.SuppressLint
import android.content.Context

class Prefs(context: Context) {
    private val prefs = context.getSharedPreferences("drink_order_prefs", Context.MODE_PRIVATE)

    var isTableInitialized: Boolean
        get() = prefs.getBoolean("isTableInitialized", false)
        @SuppressLint("UseKtx")
        set(value) = prefs.edit().putBoolean("isTableInitialized", value).apply()

    fun saveCurrentUserEmail(email: String) {
        prefs.edit().putString("current_user_email", email).apply()
    }

    fun getCurrentUserEmail(): String? {
        return prefs.getString("current_user_email", null)
    }

    fun clearCurrentUser() {
        prefs.edit().remove("current_user_email").apply()
    }
}