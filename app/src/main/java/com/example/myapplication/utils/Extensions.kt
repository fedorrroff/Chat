package com.example.myapplication.utils

import android.view.View
import com.example.myapplication.models.ChatUser
import com.example.myapplication.models.CurrentUser
import com.google.firebase.auth.FirebaseAuth

fun View.makeGone() {
    visibility = View.GONE
}

fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun CurrentUser.toChatUser() = ChatUser(
    id = this.id!!,
    name = this.name!!
)
