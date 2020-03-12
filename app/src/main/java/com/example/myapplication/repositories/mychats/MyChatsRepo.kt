package com.example.myapplication.repositories.mychats

import com.example.myapplication.domain.Resource
import com.example.myapplication.models.Chat
import com.example.myapplication.models.CurrentUser
import com.example.myapplication.utils.getValue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class MyChatsRepo @Inject constructor(

): IMyChatsRepo {

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    override suspend fun getMyChats(): Resource.Success<MutableList<Chat>> {
        val refUsers = firebaseDatabase.getReference("users").child(currentUser?.uid!!).getValue()

        val chatUsers: MutableList<String> = mutableListOf()

        val userChatList = refUsers.getValue(CurrentUser::class.java)?.chats
        chatUsers.addAll(userChatList as Iterable<String>)

        val refChats = firebaseDatabase.getReference("chats").getValue()
        val chats: MutableList<Chat> = mutableListOf()

        chatUsers.forEach {chatId ->
            refChats.children.forEach {data ->
                if(data.key.toString() == chatId) {
                    chats.add(refChats.child(data.key.toString()).getValue(Chat::class.java)!!)
                }
            }
        }

        return Resource.Success(chats)
    }
}