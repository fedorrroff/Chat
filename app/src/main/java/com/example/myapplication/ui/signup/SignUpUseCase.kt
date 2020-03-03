package com.example.myapplication.ui.signup

import android.util.Log
import com.example.myapplication.models.CurrentUser
import com.example.myapplication.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    val navigator: Navigation,
    val auth: FirebaseAuth
) {

    private val firebaseDatabase = FirebaseDatabase.getInstance()

    operator fun invoke(email: String, password: String, name: String?) = createAccount(email, password, name)

    private fun createAccount(email: String, password: String, name: String?) {
        Log.d(SignUpFragment.TAG, "createAccount:$email")

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(SignUpFragment.TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())
                    addUserToDb(user!!.uid, name)
                    navigator.showMyChatsScreen()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(SignUpFragment.TAG, "createUserWithEmail:failure", task.exception)
                }
            }
    }

    private fun addUserToDb(uid: String, displayName: String?) {
        val ref = firebaseDatabase.getReference("users").child(uid)
        val user = CurrentUser(id = uid, name = displayName, chats = listOf())
        ref.setValue(user)
    }
}