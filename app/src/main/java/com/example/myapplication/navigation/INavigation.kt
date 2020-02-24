package com.example.myapplication.navigation

import com.example.myapplication.models.Chat

interface INavigation {

    fun showSplashScreen()

    fun showLoginScreen()

    fun showChatScreen(item: Chat)

    fun showSignUpScreen()

    fun showMyChatsScreen()
}