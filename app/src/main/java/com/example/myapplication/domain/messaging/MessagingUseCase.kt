package com.example.myapplication.domain.messaging

import com.example.myapplication.domain.Resource
import com.example.myapplication.models.Chat
import com.example.myapplication.models.Message
import com.example.myapplication.repositories.messaging.IMessagingRepo
import com.google.firebase.database.*
import javax.inject.Inject

class MessagingUseCase @Inject constructor(
    private val messagesRepo: IMessagingRepo
): IMessagingUseCase {

    override suspend fun getMessages(chat: Chat, action: (msgs: MutableList<Message>) -> Unit): Resource<MutableList<Message>> = messagesRepo.getMessagesDB(chat, action)

    override suspend fun sendMessage(message: String) {
        messagesRepo.sendMessageDB(message)
    }

    override fun markMessageAsRead(messageId: Int, chaId: String) = messagesRepo.markMessageAsRead(messageId, chaId)

    companion object {
        const val TAG = "MessagingUseCase"
    }
}
