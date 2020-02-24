package com.example.myapplication.di

import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.chat.ChatFragment
import com.example.myapplication.di.viewmodels.ViewModelModule
import com.example.myapplication.login.LoginFragment
import com.example.myapplication.mychats.MyChatsFragment
import com.example.myapplication.navigation.Navigation
import com.example.myapplication.signup.SignUpFragment
import com.example.myapplication.splash.SplashFragment
import dagger.Component

@Component(modules = [ActivityModule::class, ViewModelModule::class])
interface MainComponent {

    fun activity(): AppCompatActivity

    fun navigator(): Navigation

    fun inject(fragment: LoginFragment)

    fun inject(fragment: SignUpFragment)

    fun inject(fragment: ChatFragment)

    fun inject(fragment: MyChatsFragment)

    fun inject(fragment: SplashFragment)
}