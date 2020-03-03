package com.example.myapplication.navigation

import com.example.myapplication.models.Chat
import com.example.myapplication.models.ChatUser

interface INavigation {

    fun showSplashScreen()

    fun showLoginScreen()

    fun showChatScreen(item: Chat, title: String)

    fun showSignUpScreen()

    fun showMyChatsScreen()

    fun showProfileScreen(chatUser: ChatUser)
}