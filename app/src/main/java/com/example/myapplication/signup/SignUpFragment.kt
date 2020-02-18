package com.example.myapplication.signup

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
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.android.synthetic.main.signup_fragment.*

class SignUpFragment: Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var navigator: INavigation

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.signup_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigator = Navigation(activity as AppCompatActivity)
        auth = FirebaseAuth.getInstance()

        submitRegBtn.setOnClickListener {
            createAccount(emaiEt.text.toString(),
                passwordEt.text.toString(),
                nameEt.text.toString())
        }
    }

    private fun createAccount(email: String, password: String, name: String?) {
        Log.d(TAG, "createAccount:$email")

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity as Activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(name).build())
                    navigator.showChatScreen()

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(activity, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    companion object {
        fun newInstance() = SignUpFragment()
        const val TAG = "SignUpFragment"
    }
}