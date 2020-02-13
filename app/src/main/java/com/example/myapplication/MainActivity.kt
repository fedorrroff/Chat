package com.example.myapplication

import android.app.PendingIntent.getActivity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.myapplication.database.RealtimeDatabase
import com.example.myapplication.firebasefunctions.Functions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getFirebaseInstanceId()

        sendUpstream()

        val database = RealtimeDatabase()

        database.setListener()

        database.setValue("val from main")
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
            Log.d("Main Activity", msg)
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

        msgId += 1

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
