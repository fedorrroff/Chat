package com.example.myapplication.di

import com.example.myapplication.repositories.chat.ChatRepo
import com.example.myapplication.repositories.chat.IChatRepo
import com.example.myapplication.repositories.getusers.GetUsersRepo
import com.example.myapplication.repositories.getusers.IGetUsersRepo
import com.example.myapplication.repositories.messaging.IMessagingRepo
import com.example.myapplication.repositories.messaging.MessagingRepo
import dagger.Binds
import dagger.Module

@Module
abstract class RepoModule {

    @Binds
    abstract fun bindChatRepo(repo: ChatRepo): IChatRepo

    @Binds
    abstract fun bindMessagingRepo(repo: MessagingRepo): IMessagingRepo

    @Binds
    abstract fun bindGetUsersRepo(repo: GetUsersRepo): IGetUsersRepo
}