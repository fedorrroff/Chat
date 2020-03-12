package com.example.myapplication.domain.mychats

import com.example.myapplication.domain.Resource
import com.example.myapplication.models.Chat
import com.example.myapplication.repositories.mychats.IMyChatsRepo
import javax.inject.Inject

class MyChatsUseCase @Inject constructor(
    private val myChatsRepo: IMyChatsRepo
): IMyChatsUseCase {

    override suspend fun getMyChats(): Resource.Success<MutableList<Chat>> = myChatsRepo.getMyChats()

    override fun addNewMsgListener(action: (chatId: String?) -> Unit) = myChatsRepo.addNewMsgListener(action)

    override suspend fun getChatById(chatId: String): Resource.Success<Chat?> = myChatsRepo.getChatById(chatId)
}