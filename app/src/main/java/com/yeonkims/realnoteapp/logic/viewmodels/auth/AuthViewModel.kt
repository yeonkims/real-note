package com.yeonkims.realnoteapp.logic.viewmodels.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeonkims.realnoteapp.data.enums.AuthState
import com.yeonkims.realnoteapp.data.repositories.NoteRepository
import com.yeonkims.realnoteapp.data.repositories.UserRepository
import com.yeonkims.realnoteapp.logic.viewmodels.AlertViewModel
import com.yeonkims.realnoteapp.util.dev_tools.Logger
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val noteRepository: NoteRepository,
    private val alertViewModel: AlertViewModel
) : ViewModel() {

    val currentUser = userRepository.getCurrentUser()

    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    var authState : MutableLiveData<AuthState> = MutableLiveData(null)

    fun loadInitialAuthState() {
        viewModelScope.launch {
            userRepository.fetchExistingUser {
                authState.value = it
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.logout()
            noteRepository.clearNotes()
        }
    }

    fun deleteAccount() {
        if(isLoading.value == true) {
            return
        }
        viewModelScope.launch {
            isLoading.value = true
            userRepository.deleteAccount(currentUser.value!!.email).addOnCompleteListener { task ->
                isLoading.value = false
                if(task.isSuccessful) {
                    logout()
                    alertViewModel.recordAlertMessage("Your account is now deleted.")
                }
            }
        }
    }
}