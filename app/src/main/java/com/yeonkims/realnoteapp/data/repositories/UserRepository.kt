package com.yeonkims.realnoteapp.data.repositories

import androidx.lifecycle.LiveData
import com.yeonkims.realnoteapp.data.models.User

interface UserRepository {

    suspend fun login(email: String, password: String) : Boolean

    suspend fun signUp(email: String, password: String) : Boolean

    suspend fun resetPassword(email: String) : Boolean

    suspend fun logout()

    fun getCurrentUser() : LiveData<User>
}