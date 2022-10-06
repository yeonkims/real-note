package com.yeonkims.realnoteapp.data.repositories

import androidx.lifecycle.LiveData
import com.yeonkims.realnoteapp.data.models.User
import com.yeonkims.realnoteapp.util.aliases.AuthStateFunction
import com.yeonkims.realnoteapp.util.aliases.BooleanFunction
import com.yeonkims.realnoteapp.util.aliases.BooleanStringFunction

interface UserRepository {

    suspend fun fetchExistingUser(onCompleteListener: AuthStateFunction)

    suspend fun login(email: String, password: String, onCompleteListener: BooleanFunction)

    suspend fun signUp(email: String, password: String, onCompleteListener: BooleanStringFunction)

    suspend fun resetPassword(email: String, onCompleteListener: BooleanFunction)

    suspend fun logout()

    fun getCurrentUser() : LiveData<User?>
}

