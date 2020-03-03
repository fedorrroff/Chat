package com.example.myapplication.ui.myprofile

import androidx.lifecycle.*
import javax.inject.Inject

class ProfileViewModel @Inject constructor(): ViewModel(), LifecycleObserver {

    val name = MutableLiveData<String>()
    val tag = MutableLiveData<String>()

    fun bind(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun initChats() {

    }
}