package com.example.myapplication.utils

import android.view.View
import com.example.myapplication.models.ChatUser
import com.example.myapplication.models.CurrentUser
import java.text.SimpleDateFormat
import java.util.*

fun View.makeGone() {
    visibility = View.GONE
}

fun View.makeVisible() {
    visibility = View.VISIBLE
}

fun CurrentUser.toChatUser() = ChatUser(
    id = this.id!!,
    tag = this.tag!!,
    name = this.name,
    lastName = this.lastName
)
