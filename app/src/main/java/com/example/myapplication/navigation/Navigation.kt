package com.example.myapplication.navigation

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.myapplication.R
import com.example.myapplication.chat.ChatFragment
import com.example.myapplication.login.LoginFragment
import com.example.myapplication.signup.SignUpFragment
import com.example.myapplication.splash.SplashFragment

class Navigation(activity: AppCompatActivity) : INavigation {

    private val fragmentManager: FragmentManager = activity.supportFragmentManager

    override fun showSplashScreen() {
        showFragmentMain(SplashFragment.newInstance())
    }

    override fun showLoginScreen() {
        showFragmentMain(LoginFragment.newInstance())
    }

    override fun showChatScreen() {
        showFragmentMain(ChatFragment.newInstance())
    }

    override fun showSignUpScreen() {
        showFragment(SignUpFragment.newInstance())
    }

    private fun showFragment(fragment: Fragment) {
        fragmentManager.beginTransaction()
            .replace(CONTAINER_ID, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showFragmentMain(fragment: Fragment) {
        fragmentManager.beginTransaction()
            .replace(CONTAINER_ID, fragment)
            .commit()
    }

    companion object {
        const val CONTAINER_ID = R.id.fragment_container
    }
}