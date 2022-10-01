package com.yeonkims.realnoteapp.logic.viewmodels.auth

import android.util.Log
import androidx.lifecycle.*
import com.yeonkims.realnoteapp.data.repositories.UserRepository
import com.yeonkims.realnoteapp.logic.viewmodels.AlertViewModel
import com.yeonkims.realnoteapp.util.validators.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val repository: UserRepository,
    private val alertViewModel: AlertViewModel
) : ViewModel() {

    val email : MutableLiveData<String> = MutableLiveData("a@a.com")
    val password : MutableLiveData<String> = MutableLiveData("123123123")

    val currentUser = repository.getCurrentUser()

    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    fun login() {
        if(isLoading.value == true) {
            return
        }

        val loginEmail = email.value
        val loginPassword = password.value
        val loginFields = listOf(loginEmail, loginPassword)

        val validators = listOf(
            NonEmptyFieldsValidator(loginFields),
            EmailValidator(loginEmail),
            PasswordValidator(loginPassword)
        )

        val errorMessage = validators.validate()

        if(!errorMessage.isNullOrEmpty()) {
            alertViewModel.recordAlertMessage(errorMessage)
        } else {
            viewModelScope.launch {
                isLoading.value = true

                repository.login(loginEmail!!, loginPassword!!) { isSuccess ->
                    isLoading.value = false
                    if(isSuccess) {
                        alertViewModel.recordAlertMessage("Success!")
                    } else {
                        alertViewModel.recordAlertMessage("Please check your login details")
                    }
                }

            }
        }
    }

}
