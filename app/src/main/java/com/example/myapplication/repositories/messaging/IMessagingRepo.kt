package com.example.myapplication.repositories.messaging

import com.example.myapplication.domain.Resource
import com.example.myapplication.models.Chat
import com.example.myapplication.models.Message

interface IMessagingRepo {

    suspend fun getMessagesDB(chat: Chat, action: (msgs: MutableList<Message>) -> Unit): Resource<MutableList<Message>>

    suspend fun sendMessageDB(message: String)
}