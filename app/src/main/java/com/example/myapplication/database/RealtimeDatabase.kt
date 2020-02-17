package com.example.myapplication.database

import android.util.Log
import android.view.textclassifier.ConversationActions
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.MainActivity
import com.example.myapplication.models.Message
import com.google.firebase.database.*

class RealtimeDatabase {

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.getReference("message")

    fun setValue(value: String = "Default value") {
        databaseReference
            .child("newchild")
            .setValue("suka")

    }

    fun setChildEventListener(listener: AppCompatActivity) {
        firebaseDatabase.getReference("message").addChildEventListener(object : ChildEventListener {
            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                Log.d(TAG, "Data changed: ${p1}")
                (listener as MainActivity).chatAdapter.add(listOf(Message(p0.children.lastOrNull()?.value.toString(), 2)))
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                Log.d(TAG, "Data removed: ${p0.value.toString()}")
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                Log.d(TAG, "Data added: ${p0.value.toString()}")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

    }

    fun setListener() {
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "Data updated: ${dataSnapshot.child("users").value.toString()}")
            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    fun setChildUpdateListener() {

    }

    fun pushValue(value: String, child: Int) {
        databaseReference
            .child("users")
            .child(child.toString())
            .setValue(value)
    }

    companion object {
        const val TAG = "Realtime Database"
    }
}