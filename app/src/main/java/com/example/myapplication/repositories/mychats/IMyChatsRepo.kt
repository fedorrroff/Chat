package com.example.myapplication.repositories.mychats

import com.example.myapplication.domain.Resource
import com.example.myapplication.models.Chat

interface IMyChatsRepo {

    suspend fun getMyChats(): Resource.Success<MutableList<Chat>>

}