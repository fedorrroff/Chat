package com.example.myapplication.models

data class User (
    val id: Int,
    val chats: List<Chat>
) {
}