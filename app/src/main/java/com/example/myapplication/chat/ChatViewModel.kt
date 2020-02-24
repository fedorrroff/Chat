package com.example.myapplication.chat

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.example.myapplication.models.Message
import com.example.myapplication.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val messagingUseCase: MessagingUseCase,
    val navigation: Navigation
): ViewModel(), LifecycleObserver {

    val messages = MutableLiveData<MutableList<Message>>()

    val newMessage = ObservableField<String>()

    val isButtonDisabled = object: ObservableBoolean(newMessage) {
        override fun get(): Boolean = newMessage.get().isNullOrEmpty()
    }

    fun bind(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    private fun initMessages() {
        messagingUseCase.getMessages {
            messages.postValue(it.messages)
        }
    }

    fun onSignOutButtonClicked() {
        FirebaseAuth.getInstance().signOut()
        navigation.showLoginScreen()
    }

    fun onSendButtonClicked() {
        messagingUseCase.sendMessage(newMessage.get()?.trim()  ?: "")
        newMessage.set("")
    }

    sealed class Action {
        object ScrollToBottom: Action()
    }
}