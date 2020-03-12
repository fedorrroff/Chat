package com.example.myapplication.ui.chat

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.example.myapplication.domain.messaging.MessagingUseCase
import com.example.myapplication.models.Chat
import com.example.myapplication.models.Message
import com.example.myapplication.navigation.Navigation
import com.example.myapplication.utils.Event
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val messagingUseCase: MessagingUseCase,
    val navigation: Navigation
): ViewModel(), LifecycleObserver {

    val messages = MutableLiveData<MutableList<Message>>()

    val scrollToBottomEvent = MutableLiveData<Event<Int>>()

//    val messages = liveData {
//        emit(Resource.Loading())
//        try {
//            emit(messagingUseCase.getMessages(currentChat.value!!) {
//                emit(Resource.Success(it))
//            })
//        } catch (e: Exception) {
//            emit(Resource.Failure(e))
//        }
//    }

//    val chatUsers = liveData {
//        emit(Resource.Loading())
//        try {
//            emit(getUsersUseCase.getUsersByIds(currentChat.value!!))
//        } catch (e: Exception) {
//            emit(Resource.Failure(e))
//        }
//    }

    val newMessage = ObservableField<String>()

    var currentChat = MutableLiveData<Chat>()

    val isButtonDisabled = object: ObservableBoolean(newMessage) {
        override fun get(): Boolean = newMessage.get().isNullOrEmpty()
    }

    fun bind(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun initMessages() {
        GlobalScope.launch {
            messagingUseCase.getMessages(currentChat.value!!) {
                messages.postValue(it)
            }
        }

    }

    fun onSignOutButtonClicked() {
        FirebaseAuth.getInstance().signOut()
        navigation.showLoginScreen()
    }

    fun onSendButtonClicked() {
        GlobalScope.launch {
            messagingUseCase.sendMessage(newMessage.get()?.trim()  ?: "")
            newMessage.set("")
            scrollToBottomEvent.postValue(Event(currentChat.value?.messages!!.size))
        }
    }
}