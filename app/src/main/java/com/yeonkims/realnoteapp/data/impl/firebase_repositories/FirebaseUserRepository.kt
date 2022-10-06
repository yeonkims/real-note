package com.yeonkims.realnoteapp.data.impl.firebase_repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.functions.FirebaseFunctions
import com.google.gson.Gson
import com.yeonkims.realnoteapp.data.enums.AuthState
import com.yeonkims.realnoteapp.data.models.User
import com.yeonkims.realnoteapp.data.repositories.UserRepository
import com.yeonkims.realnoteapp.util.aliases.AuthStateFunction
import com.yeonkims.realnoteapp.util.aliases.BooleanFunction
import com.yeonkims.realnoteapp.util.aliases.BooleanStringFunction
import com.yeonkims.realnoteapp.util.dev_tools.Logger
import java.util.HashMap
import javax.inject.Inject

class FirebaseUserRepository @Inject constructor(
    private val auth: FirebaseAuth,
    private val functions: FirebaseFunctions,
    private val gson: Gson
) : UserRepository {

    private val currentUser = MutableLiveData<User?>(null)

    override fun getCurrentUser(): LiveData<User?> = currentUser

    private fun loadUser(firebaseUser: FirebaseUser?, onLoadUser: (User?) -> Unit) {
        val id = firebaseUser?.uid
        if(id != null) {
            functions.getHttpsCallable("getUser").call(mapOf("id" to id))
                .addOnCompleteListener { response ->
                    val data = response.result.data
                    val jsonData = (data as HashMap<*, *>)["res"]
                    val dbSavedUser = gson.fromJson(
                        gson.toJson(jsonData),
                        Array<User>::class.java
                    ).first()
                    currentUser.value = dbSavedUser
                    onLoadUser(dbSavedUser)
                }
        } else {
            onLoadUser(null)
        }
    }

    override suspend fun fetchExistingUser(onCompleteListener: AuthStateFunction) {
        loadUser(auth.currentUser) { user ->
            if(user == null)
                onCompleteListener(AuthState.AUTH_NOT_FOUND)
            else
                onCompleteListener(AuthState.LOGGED_IN)
        }

    }

    override suspend fun login(email: String, password: String, onCompleteListener: BooleanFunction) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                loadUser(task.result.user) {
                    onCompleteListener(true)
                }
            } else {
                onCompleteListener(false)
            }
        }
    }

    override suspend fun signUp(
        email: String,
        password: String,
        onCompleteListener: BooleanStringFunction
    ) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            var errorMessage : String?
            if(task.isSuccessful) {
                val id = task.result.user?.uid
                functions.getHttpsCallable("createUser").call(mapOf("id" to id, "email" to email))
                    .addOnCompleteListener { response ->
                        if(response.isSuccessful) {
                            currentUser.value = User(id!!, email)
                        }
                        errorMessage = response.exception?.message
                        onCompleteListener(response.isSuccessful, errorMessage)
                    }
            } else {
                errorMessage = task.exception?.message
                onCompleteListener(task.isSuccessful, errorMessage)
            }
        }
    }

    override suspend fun resetPassword(email: String, onCompleteListener: BooleanFunction) {
        onCompleteListener(true)
    }


    override suspend fun logout() {
        auth.signOut()
        currentUser.value = null
    }
}