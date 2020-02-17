package com.example.myapplication.core

import android.content.Context
import android.content.SharedPreferences

class LoginPrefences {

    fun setLogedUser() {

    }

    companion object {

        private lateinit var loginPrefences: SharedPreferences

        val instance: SharedPreferences
            get() = loginPrefences

        const val NAME = "login_preferences"

        fun create(context: Context) = context.applicationContext.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }
}