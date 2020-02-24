package com.example.myapplication.models

import java.io.Serializable

data class Chat(
    val chatId: String = "",
    val messages: MutableList<Message> = mutableListOf(),
    val users: List<String> = emptyList()
) : Serializable {
}