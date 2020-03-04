package com.example.myapplication.ui.signup

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.domain.registration.RegistrationUseCase
import com.example.myapplication.domain.signup.SignUpUseCase
import com.example.myapplication.navigation.Navigation
import com.example.myapplication.repositories.registration.RegistrationRepo
import com.example.myapplication.utils.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.EmptyCoroutineContext

class SignUpViewModel @Inject constructor(
    val navigation: Navigation,
    private val registrationUseCase: RegistrationUseCase
) : ViewModel(), LifecycleObserver{

    val email: ObservableField<String> = ObservableField("")
    val password: ObservableField<String> = ObservableField("")
    val name: ObservableField<String> = ObservableField("")

    val showErrorSnackbarEvent = MutableLiveData<Event<String>>()

    fun bind(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    val buttonEnabled= object: ObservableBoolean(email, password) {
        override fun get(): Boolean = !email.get().isNullOrEmpty() && !password.get().isNullOrEmpty() && !name.get().isNullOrEmpty()
    }

    fun onSignUpButtonClicked() {
        val email = requireNotNull(email.get())
        val password = requireNotNull(password.get())
        val name = requireNotNull(name.get())
        CoroutineScope(EmptyCoroutineContext).launch {
            val code = registrationUseCase.createAccount(email, password, name)
            when (code.data) {
                RegistrationRepo.CODE_SUCCESS -> {
                    navigation.showMyChatsScreen()
                }
                RegistrationRepo.CODE_NOT_UNIQUE_NAME -> {
                    showErrorSnackbarEvent.postValue(Event("Your nickname is created, please, try another"))
                }
                RegistrationRepo.CODE_WEEK_PASSWORD -> {
                    showErrorSnackbarEvent.postValue(Event("Password must contain at least 6 symbols"))
                }
                RegistrationRepo.CODE_NOT_UNIQE_EMAIL -> {
                    showErrorSnackbarEvent.postValue(Event("The email address is already in use by another account"))
                }
                RegistrationRepo.CODE_INVALID_EMAIL_PATTERN -> {
                    showErrorSnackbarEvent.postValue(Event("Invalid email pattern"))
                }
                RegistrationRepo.CODE_ERR -> {
                    showErrorSnackbarEvent.postValue(Event("Something went wrong, please try again later"))
                }
            }
        }
    }
}