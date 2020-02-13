package com.example.myapplication.firebasefunctions

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException

class Functions {

    private var functions: FirebaseFunctions = FirebaseFunctions.getInstance()

     fun addMessage(text: String): Task<String> {
        // Create the arguments to the callable function.
        val data = hashMapOf(
            "text" to text,
            "push" to true
        )

        return functions
            .getHttpsCallable("addMessage")
            .call(data)
            .continueWith { task ->
                // This continuation runs on either success or failure, but if the task
                // has failed then result will throw an Exception which will be
                // propagated down.
                val result = task.result?.data as String
                result
            }
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    val e = it.exception
                    if (e is FirebaseFunctionsException) {
                        val code = e.code
                        val details = e.details
                    }
                    Log.e(TAG, "Error: ${e.toString()}")
                } else {
                    Log.d(TAG, "Message was added ${it.result}")
                }
            }
    }

    companion object {

        const val TAG = "Functions"

    }

}