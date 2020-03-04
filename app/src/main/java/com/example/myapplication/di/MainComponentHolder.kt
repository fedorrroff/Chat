package com.example.myapplication.di

import androidx.appcompat.app.AppCompatActivity

class MainComponentHolder {

    private lateinit var mainComponent: MainComponent

    fun getMainComponent() = mainComponent

    fun initDaggerComponent(activity: AppCompatActivity) {

        mainComponent = DaggerMainComponent.builder()
            .activityModule(ActivityModule(activity))
            .build()
    }

    companion object {
        fun getInstance() = MainComponentHolder()
    }
}