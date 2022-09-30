package com.yeonkims.realnoteapp.data.impl.temp_repositories

import androidx.lifecycle.LiveData
import com.yeonkims.realnoteapp.data.models.User
import com.yeonkims.realnoteapp.data.repositories.UserRepository
import com.yeonkims.realnoteapp.util.aliases.BooleanFunction
import javax.inject.Inject

class TempUserRepository @Inject constructor(

) : UserRepository {

    override suspend fun login(
        email: String,
        password: String,
        onCompleteListener: BooleanFunction
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun signUp(
        email: String,
        password: String,
        onCompleteListener: BooleanFunction
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun resetPassword(email: String, onCompleteListener: BooleanFunction) {
        TODO("Not yet implemented")
    }

    override suspend fun logout() {
        TODO("Not yet implemented")
    }

    override fun getCurrentUser(): LiveData<User?> {
        TODO("Not yet implemented")
    }
}