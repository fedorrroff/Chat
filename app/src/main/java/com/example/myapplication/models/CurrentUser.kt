package com.example.myapplication.models

import java.io.Serializable

data class CurrentUser (
    val id: String? = "",
    val name: String? = "",
    val chats: List<String> = emptyList()
) : Serializable