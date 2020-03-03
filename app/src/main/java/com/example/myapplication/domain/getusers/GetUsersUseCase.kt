package com.example.myapplication.domain.getusers

import com.example.myapplication.domain.Resource
import com.example.myapplication.models.Chat
import com.example.myapplication.models.CurrentUser
import com.example.myapplication.repositories.getusers.GetUsersRepo
import com.example.myapplication.repositories.getusers.IGetUsersRepo
import com.example.myapplication.ui.mychats.GetMyChatUseCase
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val getUsersRepo: IGetUsersRepo
): IGetUsersUseCase {

    override suspend fun getUsersByIds(chat: Chat): Resource.Success<MutableList<CurrentUser>> = getUsersRepo.getUsersByIds(chat)

    override suspend fun getUserByName(name: String): Resource<CurrentUser> = getUsersRepo.getUserByName(name)
}