package com.example.myapplication.domain.registration

import com.example.myapplication.domain.Resource

interface IRegistrationUseCase {

    suspend fun createAccount(email: String, password: String, name: String?): Resource.Success<String>

}