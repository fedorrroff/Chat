package com.example.myapplication.mychats

import com.example.myapplication.models.Chat
import com.example.myapplication.models.CurrentUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject
import kotlin.collections.MutableList

class GetMyChatUseCase @Inject constructor()  {

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    fun setChat() {
        val ref = firebaseDatabase.getReference("users").child(currentUser?.uid!!)
        val user = CurrentUser(currentUser.uid, listOf("testid"))
        ref.setValue(user)
    }

     fun getChatIdsList(action: (chatUsers: MutableList<Chat>) -> Unit) {
        val ref = firebaseDatabase.getReference("users").child(currentUser?.uid!!)

         val chatUsers: MutableList<String> = mutableListOf()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val user = p0.getValue(CurrentUser::class.java)?.chats
                chatUsers.addAll(user as Iterable<String>)

                val refNew = firebaseDatabase.getReference("chats")

                refNew.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        val chats: MutableList<Chat> = mutableListOf()
                            chatUsers.forEach { chatId ->
                                p0.children.forEach { data ->
                                    if (data.key.toString() == chatId) {
                                        chats.add(p0.child(data.key.toString()).getValue(Chat::class.java)!!)
                                    }
                                }
                            }

                        action(chats)
                    }
                    override fun onCancelled(p0: DatabaseError) {
                    }
                })
            }

            override fun onCancelled(p0: DatabaseError) {
            }
        })
    }
}