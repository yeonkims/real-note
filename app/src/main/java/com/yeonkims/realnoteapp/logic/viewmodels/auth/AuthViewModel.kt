package com.yeonkims.realnoteapp.logic.viewmodels.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeonkims.realnoteapp.data.enums.AuthState
import com.yeonkims.realnoteapp.data.repositories.NoteRepository
import com.yeonkims.realnoteapp.data.repositories.UserRepository
import com.yeonkims.realnoteapp.logic.viewmodels.AlertViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val noteRepository: NoteRepository,
    private val alertViewModel: AlertViewModel
) : ViewModel() {

    val currentUser = userRepository.getCurrentUser()

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
        viewModelScope.launch {
            userRepository.deleteAccount(currentUser.value!!.email).addOnCompleteListener { task ->
                if(task.isSuccessful) {
                    logout()
                    alertViewModel.recordAlertMessage("Your account is now deleted.")
                }
            }
        }
    }
}