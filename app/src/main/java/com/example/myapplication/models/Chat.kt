package com.example.myapplication.models

data class Chat(
    val mesages: List<Message>,
    val users: List<Int>,
    val sentTo: Int
) {
}