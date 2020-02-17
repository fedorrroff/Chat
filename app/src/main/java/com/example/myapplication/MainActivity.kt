package com.example.myapplication

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.chat.adapters.ChatAdapter
import com.example.myapplication.database.RealtimeDatabase
import com.example.myapplication.models.CurrentUser
import com.example.myapplication.models.Message
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val chatAdapter = ChatAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currUser = CurrentUser(1, emptyList())

        val messagesList = mutableListOf(
            Message("aaaa", 1),
            Message("bbb", 2),
            Message("ccc", 1)
        )

        messageListRecycler.layoutManager = LinearLayoutManager(this)

        chatAdapter.items = messagesList
        messageListRecycler.adapter = chatAdapter

        val database = RealtimeDatabase()

        sendBtn.isEnabled = false

        editText.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                sendBtn.isEnabled = s.toString().trim().isNotEmpty()
            }
        })

        sendBtn.setOnClickListener {
            msgId += 1
            val text = editText.text.toString()
            database.pushValue(text, msgId)
//            chatAdapter.add(listOf(Message(text, 1)))
        }

        database.setListener()
        database.setChildEventListener(this)
        database.setValue(editText.text.toString())
    }

    override fun onStart() {
        super.onStart()
        registerReseiver()
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver()
    }

    private fun unregisterReceiver() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReseiver)
    }

    private fun getFirebaseInstanceId() = FirebaseInstanceId.getInstance().instanceId
        .addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("Main Activity", "getInstanceId failed", task.exception)
                return@OnCompleteListener
            }

            // Get new Instance ID token
            val token = task.result?.token

            // Log and toast
            val msg = getString(R.string.msg_token_fmt, token)
            Log.d(TAG, msg)
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })

    private fun registerReseiver() {
        LocalBroadcastManager.getInstance(this).registerReceiver(
            mMessageReseiver, IntentFilter("getMSG")
        )
    }

    private val mMessageReseiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(TAG, "${intent?.getStringExtra("MSG")}")
        }
    }

    private fun sendUpstream() {
        val fm = FirebaseMessaging.getInstance()

        fm.send(RemoteMessage.Builder("\"$SENDER_ID@fcm.googleapis.com\"")
            .setMessageId(msgId.inc().toString())
            .addData("msg", "Сам Пошел")
            .build()
        )
    }

    companion object {
        const val TAG = "Main Activity"
        const val SENDER_ID = 1
        var msgId = 0
    }
}
