package com.yeonkims.realnoteapp.data.impl.temp_repositories

import androidx.lifecycle.LiveData
import com.yeonkims.realnoteapp.data.models.User
import com.yeonkims.realnoteapp.data.repositories.UserRepository
import javax.inject.Inject

class TempUserRepository @Inject constructor(

) : UserRepository {
    private val fakeUser = User("1", "aa@aaa.com")

    override suspend fun login(email: String, password: String): Boolean {
        if(email == fakeUser.email)
            return true
        return false
    }

    override suspend fun signUp(email: String, password: String): Boolean {
        return true
    }

    override suspend fun resetPassword(email: String): Boolean {
        return true
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }

    override fun getCurrentUser(): LiveData<User> {
        TODO("Not yet implemented")
    }
}