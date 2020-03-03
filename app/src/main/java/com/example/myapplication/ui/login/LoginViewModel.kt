package com.example.myapplication.ui.login

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.myapplication.navigation.Navigation
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val navigator: Navigation,
    val loginUseCase: LoginUseCase
): ViewModel(), LifecycleObserver {

    val email: ObservableField<String> = ObservableField("")
    val password: ObservableField<String> = ObservableField("")

    val buttonDisabled= object: ObservableBoolean(email, password) {
        override fun get(): Boolean = email.get().isNullOrEmpty() || password.get().isNullOrEmpty()
    }

    fun bind(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    fun onSignInButtonClicked() {
        val email = requireNotNull(email.get())
        val password = requireNotNull(password.get())

        loginUseCase(email, password)
    }

    fun onSignUpButtonClicked() {
        navigator.showSignUpScreen()
    }
}