package com.example.myapplication.ui.mychats

import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.example.myapplication.domain.Resource
import com.example.myapplication.domain.chat.CreateChatUseCase
import com.example.myapplication.domain.getusers.GetUsersUseCase
import com.example.myapplication.domain.mychats.MyChatsUseCase
import com.example.myapplication.models.Chat
import com.example.myapplication.models.ChatUser
import com.example.myapplication.models.CurrentUser
import com.example.myapplication.navigation.Navigation
import com.example.myapplication.repositories.mychats.MyChatsRepo
import com.example.myapplication.utils.Event
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.Exception
import kotlin.coroutines.EmptyCoroutineContext

class MyChatsViewModel @Inject constructor(
    private val myChatUseCase: MyChatsUseCase,
    private val getUsersUseCase: GetUsersUseCase,
    private val createChatUseCase: CreateChatUseCase
): ViewModel(), LifecycleObserver {

    lateinit var navigation: Navigation

    val chats = liveData {
        emit(Resource.Loading())
        try {
            emit(myChatUseCase.getMyChats())
        } catch (e: Exception) {
            emit(Resource.Failure<MutableList<Chat>>(e))
        }
    }

    val currentUser = liveData {
        emit(Resource.Loading())
        try {
            emit(getUsersUseCase.getCurrentUser())
        } catch (e: Exception) {
            emit(Resource.Failure<CurrentUser>(e))
        }
    }

    val showNoElementToastEvent = MutableLiveData<Event<String>>()
    val showSearchDialogEvent = MutableLiveData<Event<Unit>>()
    val showToastSucseedEvent = MutableLiveData<Event<String>>()
    val pushChatToTopEvent = MutableLiveData<Event<String?>>()
    val updateSingleChatEvent = MutableLiveData<Event<Chat>>()

    val desiredUser = ObservableField<String>()

    private val currentUserForVM: CurrentUser by lazy { (currentUser.value as Resource.Success).data!! }

    fun bind(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)

        myChatUseCase.addNewMsgListener {
            pushChatToTopEvent.postValue(Event(it))
            CoroutineScope(EmptyCoroutineContext).launch {
                updateSingleChatEvent.postValue(Event(myChatUseCase.getChatById(it!!).data!!))
            }
        }
    }

    fun onChatItemClicked(item: Chat) {
        val chatUsers = mutableListOf<CurrentUser>()

        CoroutineScope(EmptyCoroutineContext).launch {
            chatUsers.addAll(getUsersUseCase.getUsersByIds(item).data)

            val autorizedChatUser = chatUsers.firstOrNull {user ->
                user.id == currentUserForVM.id
            }
            val user = chatUsers.firstOrNull {currUser ->
                autorizedChatUser?.id != currUser.id
            }

            val title = user?.name + " " + (user?.lastName ?: "")
            navigation.showChatScreen(item, title)
        }
    }

    fun onFABclicked() {
        showSearchDialogEvent.postValue(Event(Unit))
    }

    fun onCreateChat() {
        CoroutineScope(EmptyCoroutineContext).launch {
            when (val res = getUsersUseCase.getUserByName(desiredUser.get() ?: "")){
                is Resource.Success -> {
                    if (currentUserForVM.tag == res.data.tag!!){
                        showNoElementToastEvent.postValue(Event("You can`t make chat with yourself"))
                    } else {
                        showToastSucseedEvent.postValue(Event("Chat created with user ${res.data.tag}"))
                        val userFullName = res.data.name + " " + (res.data.lastName)
                        navigation.showChatScreen(createChatUseCase.createOrOpenChat((currentUser.value as Resource.Success).data!!, res.data), userFullName)
                        (currentUser as MutableLiveData).postValue(getUsersUseCase.getCurrentUser())
                    }
                }
                is Resource.Failure -> {
                    showNoElementToastEvent.postValue(Event("There are no user with such tag"))
                }
            }
        }
    }

    //костыль ебаный сука опять тебя вызывать
    fun updateLastMsgs() {
        CoroutineScope(EmptyCoroutineContext).launch {
            (chats as MutableLiveData).postValue(myChatUseCase.getMyChats())
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