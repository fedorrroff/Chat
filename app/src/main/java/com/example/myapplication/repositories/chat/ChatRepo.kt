package com.example.myapplication.repositories.chat

import com.example.myapplication.models.Chat
import com.example.myapplication.models.CurrentUser
import com.example.myapplication.utils.getValue
import com.example.myapplication.utils.toChatUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.EmptyCoroutineContext

class ChatRepo @Inject constructor():  IChatRepo {

    private val firebaseDatabase = FirebaseDatabase.getInstance()

    override suspend fun createOrOpenChat(currentUser: CurrentUser, otherUser: CurrentUser): Chat {
        val newChat = Chat(
            chatId = getChatId(),
            messages = mutableListOf(),
            users = listOf(currentUser.toChatUser(), otherUser.toChatUser())
        )
        val isIdent = checkChatIdentity(newChat)

        return if (isIdent == newChat) {
            val ref = firebaseDatabase.getReference("chats/")
            ref.child(newChat.chatId).setValue(newChat)
            val refUsers = firebaseDatabase.getReference("users")
            refUsers.child("${currentUser.id}/chats/${currentUser.chats.size}").setValue(newChat.chatId)
            refUsers.child("${otherUser.id}/chats/${otherUser.chats.size}").setValue(newChat.chatId)
            newChat
        } else {
            isIdent
        }
    }

    private suspend fun getChatId(): String {
        val ref = firebaseDatabase.getReference("chats").getValue()

        return ref.children.count().toString()
    }

    private suspend fun checkChatIdentity(newChat: Chat): Chat {
        val ref = firebaseDatabase.getReference("chats").getValue()

        val userChats = getChats()

//        currentUser.chats.forEach {chatId ->
//            val chat = ref.children.firstOrNull { chatId == it.getValue(Chat::class.java)?.chatId }
//            if (chat != null) {
//                userChats.add(chat.getValue(Chat::class.java)!!)
//            }
//        }

        userChats.forEach {chat ->
            if (chat?.users?.sortedBy { it.id } == newChat.users.sortedBy{ it.id}) {
                return chat
            }
        }

        return newChat
    }

    private suspend fun getChats(): MutableList<Chat?> {
        val ref = firebaseDatabase.getReference("chats").getValue()

        val chats = mutableListOf<Chat?>()
        ref.children.forEach {
            chats.add(it.getValue(Chat::class.java))
        }

        return chats
    }
}