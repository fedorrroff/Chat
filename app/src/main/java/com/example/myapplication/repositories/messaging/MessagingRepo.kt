package com.example.myapplication.repositories.messaging

import com.example.myapplication.domain.Resource
import com.example.myapplication.models.Chat
import com.example.myapplication.models.CurrentUser
import com.example.myapplication.models.Message
import com.example.myapplication.utils.getValue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

class MessagingRepo @Inject constructor(): IMessagingRepo {

    private lateinit var currentChat: Chat
    private var msgId: Int = 0
    private val currentUser =  FirebaseAuth.getInstance().currentUser

    private val firebaseDatabase = FirebaseDatabase.getInstance()

    override suspend fun getMessagesDB(chat: Chat, action: (msgs: MutableList<Message>) -> Unit): Resource<MutableList<Message>> {
        val ref = firebaseDatabase.getReference("chats")

        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                val children=  p0.children
                val selectedChat = children.first{
                    it.key.toString() == chat.chatId
                }

                val value = selectedChat.getValue(Chat::class.java)
                if (value != null) {
                    currentChat = value
                    msgId = value.messages.size - 1
                }

                action(currentChat.messages)
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })

        val children = ref.getValue().children

        val selectedChat = children.first{
            it.key.toString() == chat.chatId
        }

        val value = selectedChat.getValue(Chat::class.java)
        if (value != null) {
            currentChat = value
            msgId = value.messages.size - 1
        }

        return Resource.Success(value!!.messages)
    }

    override suspend fun sendMessageDB(message: String) {
        val ref = firebaseDatabase.getReference("chats")
        val chatId = currentChat.chatId

        val refCurrentUser = firebaseDatabase.getReference("users").child(currentUser?.uid!!).getValue()
        refCurrentUser.apply {
            val user = getValue(CurrentUser::class.java)
            currentChat.messages.add(Message(message, currentUser.uid))
            ref.child("${chatId}/messages/${++msgId}").setValue(Message(message, currentUser.uid, senderName = user?.name))
        }
    }
}