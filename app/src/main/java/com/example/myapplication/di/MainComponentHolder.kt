package com.example.myapplication.di

import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.di.MainComponentHolder.InstanceHolder.Companion.INSTANCE

class MainComponentHolder {

    private lateinit var mainComponent: MainComponent

    fun getMainComponent() = mainComponent

    fun initDaggerComponent(activity: AppCompatActivity) {

        mainComponent = DaggerMainComponent.builder()
            .activityModule(ActivityModule(activity))
            .build()
    }

    companion object {
        fun getInstance() = INSTANCE
    }

    class InstanceHolder {
        companion object {
            val INSTANCE: MainComponentHolder = MainComponentHolder()
        }
    }
}