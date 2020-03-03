package com.example.myapplication.ui.mychats

import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.example.myapplication.domain.Resource
import com.example.myapplication.domain.chat.CreateChatUseCase
import com.example.myapplication.domain.getusers.GetUsersUseCase
import com.example.myapplication.models.Chat
import com.example.myapplication.models.ChatUser
import com.example.myapplication.models.CurrentUser
import com.example.myapplication.navigation.Navigation
import com.example.myapplication.utils.Event
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.EmptyCoroutineContext

class MyChatsViewModel @Inject constructor(
    val navigation: Navigation,
    private val myChatUseCase: GetMyChatUseCase,
    private val getUsersUseCase: GetUsersUseCase,
    private val createChatUseCase: CreateChatUseCase
): ViewModel(), LifecycleObserver {

    val chats = MutableLiveData<MutableList<Chat>>()
    val currentUser = MutableLiveData<CurrentUser>()

    val showNoElementToastEvent = MutableLiveData<Event<String>>()
    val showSearchDialogEvent = MutableLiveData<Event<Unit>>()

    val searchebleUser = ObservableField<String>()

    fun bind(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    fun onChatItemClicked(item: Chat) {
        val chatUsers = mutableListOf<CurrentUser>()

        CoroutineScope(EmptyCoroutineContext).launch {
            chatUsers.addAll(getUsersUseCase.getUsersByIds(item).data)

            val autorizedChatUser = chatUsers.firstOrNull {user ->
                user.id == currentUser.value?.id
            }
            val title = chatUsers.firstOrNull {currUser ->
                autorizedChatUser?.id != currUser.id
            }?.name.toString()

            navigation.showChatScreen(item, title)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun initChats() {
        myChatUseCase.getChatIdsList {
            it.let { chats.postValue(it) }
        }
        myChatUseCase.getCurrentUser {
            it.let { currentUser.postValue(it) }
        }
    }

    fun onFABclicked() {
        showSearchDialogEvent.postValue(Event(Unit))
    }

    fun onCreateChat() {
        GlobalScope.launch {
            when (val res = getUsersUseCase.getUserByName(searchebleUser.get() ?: "")){
                is Resource.Success -> {
                    if (currentUser.value?.name == res.data.name!!){
                        showNoElementToastEvent.postValue(Event("You can`t make chat with yourself"))
                    } else {
                        navigation.showChatScreen(createChatUseCase.createOrOpenChat(currentUser.value!!, res.data), res.data.name)
                    }
                }
                is Resource.Failure -> {
                    showNoElementToastEvent.postValue(Event("There are no user with such name"))
                }
            }
        }
    }

    fun onSignOutButtonClicked() {
        FirebaseAuth.getInstance().signOut()
        navigation.showLoginScreen()
    }

    fun onProfileButtonClicked(chatUser: ChatUser) {
        navigation.showProfileScreen(chatUser)
    }
}