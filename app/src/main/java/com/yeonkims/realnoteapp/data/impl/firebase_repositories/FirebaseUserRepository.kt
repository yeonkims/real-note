package com.yeonkims.realnoteapp.data.impl.firebase_repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.functions.FirebaseFunctions
import com.google.gson.Gson
import com.yeonkims.realnoteapp.data.models.Note
import com.yeonkims.realnoteapp.data.models.User
import com.yeonkims.realnoteapp.data.repositories.UserRepository
import com.yeonkims.realnoteapp.util.aliases.BooleanFunction
import java.util.HashMap
import javax.inject.Inject

class FirebaseUserRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val functions: FirebaseFunctions,
    private val gson: Gson
) : UserRepository {

    private val currentUser = MutableLiveData<User>(null)

    override fun getCurrentUser(): LiveData<User> {
        return currentUser
    }

    override suspend fun login(email: String, password: String, onCompleteListener: BooleanFunction) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val id = task.result.user?.uid

                functions.getHttpsCallable("getUser").call(mapOf("id" to id))
                    .addOnCompleteListener { response ->
                        val data = response.result.data
                        val jsonData = (data as HashMap<*, *>)["res"]
                        val dbSavedUser = gson.fromJson(
                            gson.toJson(jsonData),
                            Array<User>::class.java
                        ).first()
                        currentUser.value = dbSavedUser
                        onCompleteListener(response.isSuccessful)
                    }
            } else {
                onCompleteListener(task.isSuccessful)
            }
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        onCompleteListener: BooleanFunction
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if(task.isSuccessful) {
                val id = task.result.user?.uid
                functions.getHttpsCallable("createUser").call(mapOf("id" to id, "email" to email))
                    .addOnCompleteListener { response ->
                        if(response.isSuccessful) {
                            currentUser.value = User(id!!, email)
                        }
                        onCompleteListener(response.isSuccessful)
                    }
            } else {
                onCompleteListener(task.isSuccessful)
            }
        }
    }

    override suspend fun resetPassword(email: String, onCompleteListener: BooleanFunction) {
        onCompleteListener(true)
    }


    override suspend fun logout() {
        // currentUser.value = null
    }
}