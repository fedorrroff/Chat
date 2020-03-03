package com.example.myapplication.repositories.getusers

import android.database.CursorIndexOutOfBoundsException
import com.example.myapplication.domain.Resource
import com.example.myapplication.models.Chat
import com.example.myapplication.models.CurrentUser
import com.example.myapplication.utils.getValue
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception
import javax.inject.Inject

class GetUsersRepo @Inject constructor(): IGetUsersRepo {

    private val firebaseDatabase = FirebaseDatabase.getInstance()

    override suspend fun getUsersByIds(chat: Chat): Resource.Success<MutableList<CurrentUser>> {
        val ref = firebaseDatabase.getReference("users").getValue()
        val users = mutableListOf<CurrentUser>()

        ref.apply {
            val ids = chat.users

            ids.forEach {chatUser ->
                users.add(
                    this.children.firstOrNull {
                        val value = it.getValue(CurrentUser::class.java)
                        value?.id == chatUser.id
                    }?.getValue((CurrentUser::class.java))!!
                )
            }
        }

        return Resource.Success(users)
    }

    override suspend fun getUserByName(name: String): Resource<CurrentUser> {
        val ref = firebaseDatabase.getReference("users")

        val user = ref.getValue().children.firstOrNull {
            val value = it.getValue(CurrentUser::class.java)
            value?.name == name
        }?.getValue(CurrentUser::class.java)

        return if (user != null) {
            Resource.Success(user)
        } else {
            Resource.Failure(Exception())
        }
    }
}