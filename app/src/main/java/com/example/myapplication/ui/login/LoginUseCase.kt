package com.example.myapplication.ui.login

import android.util.Log
import com.example.myapplication.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    val navigator: Navigation,
    val auth: FirebaseAuth) {

    operator fun invoke(email: String, password: String) = signIn(email, password)

    private fun signIn(email: String, password: String) {
        Log.d(LoginFragment.TAG, "signIn:$email")

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(LoginFragment.TAG, "signInWithEmail:success")
                    navigator.showMyChatsScreen()
                } else {
                    Log.w(LoginFragment.TAG, "signInWithEmail:failure", task.exception)
                }
            }
    }
}