package com.example.myapplication.models

data class Chat(
    val mesages: List<Message>,
    val to: User
) {
}