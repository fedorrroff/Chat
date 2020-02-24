package com.example.myapplication.chat

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.ChatActivity
import com.example.myapplication.R
import com.example.myapplication.chat.adapters.ChatAdapter
import com.example.myapplication.database.RealtimeDatabase
import com.example.myapplication.models.Message
import com.example.myapplication.navigation.INavigation
import com.example.myapplication.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.chat_fragment.*

class ChatFragment: Fragment() {

    val chatAdapter = ChatAdapter()

    private lateinit var auth: FirebaseAuth
    private lateinit var navigator: INavigation

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.chat_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigator = Navigation(activity as AppCompatActivity)
//        val currUser = CurrentUser(1, emptyList())

        val messagesList = mutableListOf(
            Message("aaaa", "1"),
            Message("bbb", "2"),
            Message("ccc", "2")
        )

        messageListRecycler.layoutManager = LinearLayoutManager(activity)

        chatAdapter.items = messagesList
        messageListRecycler.adapter = chatAdapter

        val database = RealtimeDatabase()

        sendBtn.isEnabled = false

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                sendBtn.isEnabled = s.toString().trim().isNotEmpty()
            }
        })

        sendBtn.setOnClickListener {
            ChatActivity.msgId += 1
            val text = editText.text.toString()
            database.pushValue(text, ChatActivity.msgId)
//            chatAdapter.add(listOf(Message(text, 1)))
        }

        signOutBtn.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            navigator.showLoginScreen()
        }

        database.setListener()
        database.setChildEventListener(this)
        database.setValue(editText.text.toString())
    }

    fun getAuthorizedUser() = FirebaseAuth.getInstance().currentUser?.uid

    companion object {
        fun newInstance() = ChatFragment()
    }
}