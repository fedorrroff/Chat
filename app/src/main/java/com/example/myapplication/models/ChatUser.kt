package com.example.myapplication.models

import java.io.Serializable

data class ChatUser(
    val id: String = "",
    val tag: String = "",
    val name: String = "",
    val lastName: String = ""
): Serializable