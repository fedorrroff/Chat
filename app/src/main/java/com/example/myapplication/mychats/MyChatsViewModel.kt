package com.example.myapplication.mychats

import androidx.lifecycle.*
import com.example.myapplication.models.Chat
import com.example.myapplication.navigation.Navigation
import javax.inject.Inject

class MyChatsViewModel @Inject constructor(
    val navigation: Navigation,
    val myChatUseCase: GetMyChatUseCase
): ViewModel(), LifecycleObserver {

    val chats = MutableLiveData<MutableList<Chat>>()

    fun bind(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    fun onChatItemClicked(item: Chat) {
        navigation.showChatScreen(item)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun initChats() {
        myChatUseCase.setChat()
        myChatUseCase.getChatIdsList {
            it.let { chats.postValue(it) }
        }
//        myChatUseCase.getChats {
//            chats.postValue(it)
//        }
    }
}