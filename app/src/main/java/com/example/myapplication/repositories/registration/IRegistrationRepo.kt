package com.example.myapplication.repositories.registration

import com.example.myapplication.domain.Resource

interface IRegistrationRepo {

    suspend fun createAccount(email: String,
                              password: String,
                              tag: String?,
                              firstName: String,
                              lastName: String): Resource.Success<String>

}