package com.example.myapplication.domain.chat

import com.example.myapplication.models.Chat
import com.example.myapplication.models.CurrentUser
import com.example.myapplication.repositories.chat.IChatRepo
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class CreateChatUseCase @Inject constructor(
    private val chatRepo: IChatRepo
): ICreateChatUseCase{

    override suspend fun createOrOpenChat(currentUser: CurrentUser, otherUser: CurrentUser): Chat = chatRepo.createOrOpenChat(currentUser, otherUser)
}