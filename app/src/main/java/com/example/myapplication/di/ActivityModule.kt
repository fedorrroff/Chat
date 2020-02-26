package com.example.myapplication.di

import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.navigation.INavigation
import com.example.myapplication.navigation.Navigation
import com.example.myapplication.toolbar.IMenuDelegate
import com.example.myapplication.toolbar.MenuDelegate
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides

@Module
class ActivityModule(activity: AppCompatActivity) {

    private val mActivity: AppCompatActivity = activity

    @Provides
    fun provideActivity() : AppCompatActivity = mActivity

    @Provides
    fun provideNavigator() : Navigation = Navigation(mActivity)

    @Provides
    fun provideFirebaseAuth() : FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun provideMenuDelegate() : MenuDelegate = MenuDelegate(mActivity)
}