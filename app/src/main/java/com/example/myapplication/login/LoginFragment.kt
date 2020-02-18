package com.example.myapplication.login

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.navigation.INavigation
import com.example.myapplication.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login_fragment3.*

class LoginFragment: Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navigator: INavigation

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment3, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigator = Navigation(activity as AppCompatActivity)

        auth = FirebaseAuth.getInstance()

        signIn.setOnClickListener {
            signIn(loginEt.text.toString(), passwordEt.text.toString())
        }

        registerBtn.setOnClickListener {
            onSignUpClicked()
        }
    }

    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn:$email")

        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity as Activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    navigator.showChatScreen()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(activity, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
        // [END sign_in_with_email]
    }

    private fun onSignUpClicked() {
        navigator.showSignUpScreen()
    }

    companion object {
        fun newInstance() = LoginFragment()

        const val TAG = "LoginFragment"
    }
}