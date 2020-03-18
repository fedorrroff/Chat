package com.example.myapplication.domain.mychats

import com.example.myapplication.domain.Resource
import com.example.myapplication.models.Chat

interface IMyChatsUseCase {

    suspend fun getMyChats(): Resource.Success<MutableList<Chat>>

    fun addNewMsgListener(action: (chatId: String?) -> Unit)

    suspend fun getChatById(chatId: String): Resource.Success<Chat?>
}