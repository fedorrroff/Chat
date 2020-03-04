package com.example.myapplication.domain.signup

interface ISignupUseCase {

    fun createAccount(email: String, password: String, name: String?)

}