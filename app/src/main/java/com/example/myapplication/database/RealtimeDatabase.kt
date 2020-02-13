package com.example.myapplication.database

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RealtimeDatabase {

    private val firebaseDatabase = FirebaseDatabase.getInstance()
    private val databaseReference = firebaseDatabase.getReference("message")

    fun setValue(value: String = "Default value") {
        databaseReference.setValue(value)
        databaseReference.child("users").child("1").child("username").setValue("name")
    }

    fun setListener() {
        databaseReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "Data updated: ${dataSnapshot.child("users").child("1").value.toString()}")

            }

            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    companion object {
        const val TAG = "Realtime Database"
    }
}