package com.yeonkims.realnoteapp.data.impl.firebase_repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.TaskCompletionSource
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
import java.lang.Exception
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

    override suspend fun login(email: String, password: String) : Task<Boolean> {

        val taskCreator = TaskCompletionSource<Boolean>()

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { signInTask ->
            if (signInTask.isSuccessful) {
                loadUser(signInTask.result.user) {
                    taskCreator.setResult(true)
                }
            } else {
                taskCreator.setException(signInTask.exception!!)
            }
        }
        return taskCreator.task
    }

    override suspend fun signUp(
        email: String,
        password: String,
    ) : Task<Boolean> {

        val taskCreator = TaskCompletionSource<Boolean>()

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { signUpTask ->
            if(signUpTask.isSuccessful) {
                val id = signUpTask.result.user?.uid
                functions.getHttpsCallable("createUser").call(mapOf("id" to id, "email" to email))
                    .addOnCompleteListener { createUserTask ->
                        if(createUserTask.isSuccessful) {
                            currentUser.value = User(id!!, email)
                            taskCreator.setResult(true)
                        } else {
                            taskCreator.setException(createUserTask.exception!!)
                        }
                    }
            } else {
                taskCreator.setException(signUpTask.exception!!)
            }
        }
        return taskCreator.task
    }

    override suspend fun resetPassword(email: String, onCompleteListener: BooleanFunction) {
        onCompleteListener(true)
    }


    override suspend fun logout() {
        auth.signOut()
        currentUser.value = null
    }
}