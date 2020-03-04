package com.example.myapplication.domain.mychats

import com.example.myapplication.domain.Resource
import com.example.myapplication.models.Chat

interface IMyChatsUseCase {

    suspend fun getMyChats(): Resource.Success<MutableList<Chat>>
}