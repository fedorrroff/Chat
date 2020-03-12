package com.example.myapplication.navigation

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.myapplication.R
import com.example.myapplication.ui.chat.ChatFragment
import com.example.myapplication.ui.login.LoginFragment
import com.example.myapplication.models.Chat
import com.example.myapplication.models.ChatUser
import com.example.myapplication.ui.mychats.MyChatsFragment
import com.example.myapplication.ui.myprofile.ProfileFragment
import com.example.myapplication.ui.signup.SignUpFragment
import com.example.myapplication.ui.splash.SplashFragment

class Navigation(activity: AppCompatActivity) : INavigation {

    private val CONTAINER_ID = R.id.fragment_container
    private val fragmentManager: FragmentManager = activity.supportFragmentManager

    override fun showSplashScreen() {
        showFragmentMain(SplashFragment.newInstance())
    }

    override fun showLoginScreen() {
        showFragmentMain(LoginFragment.newInstance())
    }

    override fun showChatScreen(item: Chat, title: String) {
        showFragment(ChatFragment.newInstance(item, title))
    }

    override fun showSignUpScreen() {
        showFragment(SignUpFragment.newInstance())
    }

    override fun showMyChatsScreen() {
        showFragmentMain(MyChatsFragment.newInstance())
    }

    override fun showProfileScreen(chatUser: ChatUser) {
        showFragment(ProfileFragment.newInstance(chatUser))
    }

    private fun showFragment(fragment: Fragment) {
        fragmentManager
            .beginTransaction()
            .replace(CONTAINER_ID, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun showFragmentMain(fragment: Fragment) {
        fragmentManager
            .beginTransaction()
            .replace(CONTAINER_ID, fragment)
            .commit()
    }
}