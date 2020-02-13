package com.example.myapplication.models

import java.sql.Time

data class Message (
    val messages: String,
    val sender: User,
    val time: Time
){
}