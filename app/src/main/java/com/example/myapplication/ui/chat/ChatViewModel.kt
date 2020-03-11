package com.example.myapplication.ui.chat

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.example.myapplication.domain.messaging.MessagingUseCase
import com.example.myapplication.models.Chat
import com.example.myapplication.models.Message
import com.example.myapplication.navigation.INavigation
import com.example.myapplication.navigation.Navigation
import com.example.myapplication.utils.Event
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class ChatViewModel @Inject constructor(
    private val messagingUseCase: MessagingUseCase
): ViewModel(), LifecycleObserver {

    lateinit var navigation: INavigation

    val messages = MutableLiveData<MutableList<Message>>()

    val scrollToBottomEvent = MutableLiveData<Event<Int>>()

    val newMessage = ObservableField<String>()

    var currentChat = MutableLiveData<Chat>()

    private var messageCount: Int = 0

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
                messageCount = it.size
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
            scrollToBottomEvent.postValue(Event(messageCount))
        }
    }
}