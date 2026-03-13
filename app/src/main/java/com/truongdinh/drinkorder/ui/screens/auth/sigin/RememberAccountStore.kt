package com.truongdinh.drinkorder.ui.screens.auth.sigin

import android.annotation.SuppressLint
import android.content.Context

class RememberAccountStore(
    context: Context
) {
    private val prefs = context.getSharedPreferences("remember_account_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_REMEMBER_EMAIL = "remember_email"
    }

    @SuppressLint("CommitPrefEdits")
    fun saveEmail(email: String) {
        prefs.edit().putString(KEY_REMEMBER_EMAIL, email).apply()
    }

    fun getEmail(): String? {
        return prefs.getString(KEY_REMEMBER_EMAIL, null)
    }

    @SuppressLint("UseKtx")
    fun clear() {
        prefs.edit().remove(KEY_REMEMBER_EMAIL).apply()
    }
}