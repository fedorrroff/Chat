package com.example.myapplication.models

data class CurrentUser (
    val id: Int,
    val chats: List<Chat>
)