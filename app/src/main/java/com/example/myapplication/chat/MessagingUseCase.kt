package com.example.myapplication.chat

import android.util.Log
import com.example.myapplication.models.Chat
import com.example.myapplication.models.Message
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import javax.inject.Inject

class MessagingUseCase @Inject constructor() {

    private var isFirstAttach = true
    private lateinit var currentChat: Chat

    private var msgId: Int = 0

    private val firebaseDatabase = FirebaseDatabase.getInstance()

    operator fun invoke(){}

    fun sendMessage(message: String) {
        val ref = firebaseDatabase.getReference("chats")

        val chat = Chat(
            users = listOf("ww", "ewf"),
            chatId = "testid"
        )

        val chatId = currentChat.chatId

        currentChat.messages.add(Message(message, FirebaseAuth.getInstance().currentUser?.uid))
        ref.child("${chatId}/messages/${++msgId}").setValue(Message(message, FirebaseAuth.getInstance().currentUser?.uid))
    }

    fun getMessages(action: (value: Chat) -> Unit) {
        val ref = firebaseDatabase.getReference("chats")

        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {

                val vlll = p0.children
                val tst = vlll.first()

//                if(isFirstAttach) {
                    isFirstAttach = false
                    val value = tst.getValue(Chat::class.java)

                    if (value != null) {
                        currentChat = value
                        msgId = value.messages.size - 1
                    }
//                }

//                setClildEventListener(ref, action)

                val fakeChat = Chat(
                    messages = mutableListOf(Message("a", "ewqd"), Message("b", FirebaseAuth.getInstance().currentUser?.uid)),
                    users = listOf("ww", "ewf"),
                    chatId = "wdqdq"
                )

                action(currentChat)
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.e(TAG, p0.details)
            }
        })
    }

    private fun setClildEventListener (ref: DatabaseReference, action: (value: Chat) -> Unit) {
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val value = p0.getValue(Chat::class.java)

                if (value != null) {
                    currentChat = value
                    msgId = value.messages.size - 1
                }

                action(currentChat)
            }

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }
        })
    }

    companion object {
        const val TAG = "MessagingUseCase"
    }
}
