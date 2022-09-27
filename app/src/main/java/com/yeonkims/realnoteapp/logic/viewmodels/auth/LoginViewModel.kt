package com.yeonkims.realnoteapp.logic.viewmodels.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeonkims.realnoteapp.data.impl.temp_repositories.TempUserRepository
import com.yeonkims.realnoteapp.logic.viewmodels.ErrorViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val repository: TempUserRepository,
    private val errorViewModel: ErrorViewModel
) : ViewModel() {

    val email : MutableLiveData<String> = MutableLiveData("")
    val password : MutableLiveData<String> = MutableLiveData("")

    fun login() {
        val loginEmail = email.value
        val loginPassword = password.value

        if(loginEmail.isNullOrEmpty() || loginPassword.isNullOrEmpty()) {
            errorViewModel.recordErrorMessage("Please enter valid email + password")
            return
        }

        viewModelScope.launch {
            val isSuccess = repository.login(loginEmail, loginPassword)

            if(isSuccess) {
                errorViewModel.recordErrorMessage("Success!")

            } else {
                errorViewModel.recordErrorMessage("Please check your login details")
            }
        }
    }

}