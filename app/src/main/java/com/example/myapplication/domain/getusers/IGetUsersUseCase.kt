package com.example.myapplication.domain.getusers

import com.example.myapplication.domain.Resource
import com.example.myapplication.models.Chat
import com.example.myapplication.models.CurrentUser

interface IGetUsersUseCase {

    suspend fun getUsersByIds(chat: Chat): Resource.Success<MutableList<CurrentUser>>

    suspend fun getUserByName(name: String): Resource<CurrentUser>

    suspend fun getCurrentUser(): Resource.Success<CurrentUser?>
}