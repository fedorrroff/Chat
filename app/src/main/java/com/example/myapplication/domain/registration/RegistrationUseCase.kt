package com.example.myapplication.domain.registration

import com.example.myapplication.domain.Resource
import com.example.myapplication.repositories.registration.IRegistrationRepo
import javax.inject.Inject

class RegistrationUseCase @Inject constructor(
    private val registrationRepo: IRegistrationRepo
) : IRegistrationUseCase{

    override suspend fun createAccount(
        email: String,
        password: String,
        tag: String?,
        firstName: String,
        lastName: String
    ): Resource.Success<String> = registrationRepo.createAccount(email, password, tag, firstName, lastName)
}