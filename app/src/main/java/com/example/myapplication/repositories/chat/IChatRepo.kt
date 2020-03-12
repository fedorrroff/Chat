package com.example.myapplication.repositories.chat

import com.example.myapplication.models.Chat
import com.example.myapplication.models.CurrentUser

interface IChatRepo {

    suspend fun createOrOpenChat(currentUser: CurrentUser, otherUser: CurrentUser): Chat
}