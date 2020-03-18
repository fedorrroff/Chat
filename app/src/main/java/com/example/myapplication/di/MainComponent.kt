package com.example.myapplication.di

import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.ui.chat.ChatFragment
import com.example.myapplication.di.viewmodels.ViewModelModule
import com.example.myapplication.ui.login.LoginFragment
import com.example.myapplication.ui.mychats.MyChatsFragment
import com.example.myapplication.ui.mychats.SearchUserDialogFragment
import com.example.myapplication.ui.myprofile.ProfileFragment
import com.example.myapplication.navigation.Navigation
import com.example.myapplication.ui.signup.SignUpFragment
import com.example.myapplication.ui.splash.SplashFragment
import com.example.myapplication.toolbar.MenuDelegate
import com.example.myapplication.ui.mychats.MyChatsViewModel
import dagger.Component

@Component(modules = [ActivityModule::class, ViewModelModule::class, RepoModule::class])
interface MainComponent {

    fun activity(): AppCompatActivity

    fun navigator(): Navigation

    fun menuDelegate(): MenuDelegate

    fun inject(activity: AppCompatActivity)

    fun inject(fragment: LoginFragment)

    fun inject(fragment: SignUpFragment)

    fun inject(fragment: ChatFragment)

    fun inject(fragment: MyChatsFragment)

    fun inject(fragment: SplashFragment)

    fun inject(fragment: ProfileFragment)

    fun inject(fragment: SearchUserDialogFragment)

}