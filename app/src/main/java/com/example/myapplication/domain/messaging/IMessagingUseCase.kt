package com.example.myapplication.domain.messaging

import com.example.myapplication.domain.Resource
import com.example.myapplication.models.Chat
import com.example.myapplication.models.Message

interface IMessagingUseCase {

    suspend fun getMessages(chat: Chat, action: (msgs: MutableList<Message>) -> Unit): Resource<MutableList<Message>>

    suspend fun sendMessage(message: String)

    fun markMessageAsRead(messageId: Int, chaId: String)
}