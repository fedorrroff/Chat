package com.example.myapplication.models

data class CurrentUser (
    val id: String? = "",
    val name: String? = "",
    val chats: List<String> = emptyList()
)