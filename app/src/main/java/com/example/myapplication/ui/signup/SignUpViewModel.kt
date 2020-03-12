package com.example.myapplication.ui.signup

import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.example.myapplication.domain.signup.SignUpUseCase
import com.example.myapplication.navigation.Navigation
import javax.inject.Inject

class SignUpViewModel @Inject constructor(
    val navigation: Navigation,
    val signUpUseCase: SignUpUseCase
) : ViewModel(), LifecycleObserver{

    val email: ObservableField<String> = ObservableField("")
    val password: ObservableField<String> = ObservableField("")
    val name: ObservableField<String> = ObservableField("")

    fun bind(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    fun onSignUpButtonClicked() {
        val email = requireNotNull(email.get())
        val password = requireNotNull(password.get())
        val name = requireNotNull(name.get())
        signUpUseCase.createAccount(email, password, name)
    }
}