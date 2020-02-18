package com.example.myapplication.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.navigation.INavigation
import com.example.myapplication.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth

class SplashFragment: Fragment() {

    private lateinit var navigator: INavigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.splash_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navigator = Navigation(activity as AppCompatActivity)

        view.postDelayed( {
            if (!isUserAutorized()) {
                navigator.showLoginScreen()
            } else {
                navigator.showChatScreen()
            }
        }, 2000)
    }

    private fun isUserAutorized() = FirebaseAuth.getInstance().currentUser != null

    companion object {
        fun newInstance() = SplashFragment()
    }
}