package com.example.myapplication.repositories.registration

import com.example.myapplication.domain.Resource

interface IRegistrationRepo {

    suspend fun createAccount(email: String, password: String, name: String?): Resource.Success<String>

}