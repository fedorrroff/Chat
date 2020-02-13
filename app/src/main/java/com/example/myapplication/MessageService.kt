package com.example.myapplication

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import android.os.Bundle
import android.content.Intent

class MessageService : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        Log.d(TAG, "From: ${p0.from}")
        Log.d(TAG, "Text: ${p0.notification?.body}")

        sendMessageToActivity(p0.notification?.body!!)
    }

    private fun sendMessageToActivity( msg: String) {
        val intent = Intent("getMSG")
        // You can also include some extra data.
        intent.putExtra("MSG", msg)

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }

    override fun onMessageSent(p0: String) {
        super.onMessageSent(p0)

        Log.d(TAG, "Message sent: ${p0}")
    }


    companion object {
        const val TAG = "MessageService"
    }
}