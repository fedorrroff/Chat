package com.example.myapplication.repositories.mychats

import com.example.myapplication.domain.Resource
import com.example.myapplication.models.Chat
import com.example.myapplication.models.CurrentUser
import com.example.myapplication.utils.getValue
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
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

    suspend fun getChatById(chatId: String): Resource.Success<Chat?>{
        val ref = firebaseDatabase.getReference("chats/${chatId}").getValue()

        val chat = ref.getValue(Chat::class.java)

        return Resource.Success(chat)
    }

    //перенести в интерфейс
    fun addNewMsgListener(action: (chatId: String?) -> Unit) {
        val ref = firebaseDatabase.getReference("chats")

        ref.addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                action(p0.key)
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }
}