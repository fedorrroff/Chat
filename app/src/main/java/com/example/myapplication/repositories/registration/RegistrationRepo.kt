package com.example.myapplication.repositories.registration

import android.util.Log
import com.example.myapplication.domain.Resource
import com.example.myapplication.models.CurrentUser
import com.example.myapplication.ui.signup.SignUpFragment
import com.example.myapplication.utils.getValue
import com.google.firebase.auth.*
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RegistrationRepo @Inject constructor(
    val auth: FirebaseAuth
) : IRegistrationRepo {

    private val firebaseDatabase = FirebaseDatabase.getInstance()

    override suspend fun createAccount(email: String, password: String, name: String?) : Resource.Success<String> {
        Log.d(SignUpFragment.TAG, "createAccount:$email")

        var code = ""

        val isUnique = checkUserIdentity(name!!)

        if (isUnique) {
            try {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser
                            user?.updateProfile(UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build())
                            addUserToDb(user!!.uid, name)
                            code = CODE_SUCCESS
                        }
                    }.await()
            } catch (e: FirebaseAuthException) {
                code = when (e) {
                    is FirebaseAuthWeakPasswordException ->
                        CODE_WEEK_PASSWORD
                    is FirebaseAuthUserCollisionException ->
                        CODE_NOT_UNIQE_EMAIL
                    is FirebaseAuthInvalidCredentialsException ->
                        CODE_INVALID_EMAIL_PATTERN
                    else ->
                        CODE_ERR
                }
            }
            return Resource.Success(code)
        } else {
            return Resource.Success(CODE_NOT_UNIQUE_NAME)
        }
    }

    private suspend fun checkUserIdentity(name: String): Boolean {
        val ref = firebaseDatabase.getReference("users").getValue()

        ref.children.forEach {
            val user = it.getValue(CurrentUser::class.java)
            if (user?.name?.toLowerCase() == name.toLowerCase()) {
                return false
            }
        }

        return true
    }

    private fun addUserToDb(uid: String, displayName: String?) {
        val ref = firebaseDatabase.getReference("users").child(uid)
        val user = CurrentUser(id = uid, name = displayName, chats = listOf())
        ref.setValue(user)
    }

    companion object {
        const val CODE_SUCCESS = "success"
        const val CODE_NOT_UNIQUE_NAME = "not unique name"
        const val CODE_NOT_UNIQE_EMAIL = "not unique email"
        const val CODE_WEEK_PASSWORD = "week password"
        const val CODE_INVALID_EMAIL_PATTERN = "invalid email pattern"
        const val CODE_ERR = "err"
    }
}