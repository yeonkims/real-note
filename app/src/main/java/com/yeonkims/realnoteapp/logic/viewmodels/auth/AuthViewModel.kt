package com.yeonkims.realnoteapp.logic.viewmodels.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeonkims.realnoteapp.data.enums.AuthState
import com.yeonkims.realnoteapp.data.repositories.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthViewModel @Inject constructor(
    private val repository: UserRepository,
) : ViewModel() {

    var authState : MutableLiveData<AuthState> = MutableLiveData(null)

    fun loadInitialAuthState() {
        viewModelScope.launch {
            repository.fetchExistingUser {
                authState.value = it
            }
        }
    }
}