package com.example.myapplication.domain.chat

import com.example.myapplication.models.Chat
import com.example.myapplication.models.CurrentUser

interface ICreateChatUseCase {

    suspend fun createOrOpenChat(currentUser: CurrentUser, otherUser: CurrentUser): Chat
}