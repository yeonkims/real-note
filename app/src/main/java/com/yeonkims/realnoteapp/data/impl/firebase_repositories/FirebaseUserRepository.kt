package com.yeonkims.realnoteapp.data.impl.firebase_repositories

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.yeonkims.realnoteapp.data.models.User
import com.yeonkims.realnoteapp.data.repositories.UserRepository
import com.yeonkims.realnoteapp.util.aliases.BooleanFunction
import javax.inject.Inject

class FirebaseUserRepository @Inject constructor(
    private val auth: FirebaseAuth
) : UserRepository {

    override suspend fun login(email: String, password: String, onCompleteListener: BooleanFunction) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if(task.isSuccessful) {
                val result = task.result
                Log.i(javaClass.simpleName, "SUCCESS ${result.user?.uid}")
            }
            else {
                task.exception?.message
                Log.i(javaClass.simpleName, "FAILED ${task.exception?.javaClass?.simpleName}")
                Log.i(javaClass.simpleName, task.exception?.message.toString())
            }
            onCompleteListener(task.isSuccessful)
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        onCompleteListener: BooleanFunction
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if(task.isSuccessful) {
                val result = task.result
                Log.i(javaClass.simpleName, "SUCCESS ${result.user?.uid}")
            }
            else {
                task.exception?.message
                Log.i(javaClass.simpleName, "FAILED ${task.exception?.javaClass?.simpleName}")
                Log.i(javaClass.simpleName, task.exception?.message.toString())
            }
            onCompleteListener(task.isSuccessful)
        }
    }

    override suspend fun resetPassword(email: String, onCompleteListener: BooleanFunction) {
        onCompleteListener(true)
    }


    override suspend fun logout() {
        TODO("Not yet implemented")
    }

    override fun getCurrentUser(): LiveData<User> {
        TODO("Not yet implemented")
    }
}