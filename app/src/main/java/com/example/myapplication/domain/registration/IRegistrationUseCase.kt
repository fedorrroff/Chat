package com.example.myapplication.domain.registration

import com.example.myapplication.domain.Resource

interface IRegistrationUseCase {

    suspend fun createAccount(
        email: String,
        password: String,
        tag: String?,
        firstName: String,
        lastName: String
    ): Resource.Success<String>
}