package com.example.myapplication.models

import java.io.Serializable

data class Message (
    val message: String = "",
    val senderId: String? = "",
    val senderName: String? = ""
): Serializable