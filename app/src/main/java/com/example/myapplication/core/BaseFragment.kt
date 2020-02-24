package com.example.myapplication.core

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.myapplication.di.ActivityModule
import com.example.myapplication.di.DaggerMainComponent
import com.example.myapplication.di.MainComponent

abstract class BaseFragment: Fragment() {

    protected fun getMainComponent(): MainComponent = DaggerMainComponent
                                                        .builder()
                                                        .activityModule(ActivityModule(requireActivity() as AppCompatActivity))
                                                        .build()
}