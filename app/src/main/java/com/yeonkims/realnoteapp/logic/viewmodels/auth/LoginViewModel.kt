package com.yeonkims.realnoteapp.logic.viewmodels.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeonkims.realnoteapp.data.repositories.UserRepository
import com.yeonkims.realnoteapp.logic.viewmodels.AlertViewModel
import com.yeonkims.realnoteapp.util.validators.EmailValidator
import com.yeonkims.realnoteapp.util.validators.NonEmptyFieldsValidator
import com.yeonkims.realnoteapp.util.validators.PasswordValidator
import com.yeonkims.realnoteapp.util.validators.validate
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val repository: UserRepository,
    private val alertViewModel: AlertViewModel
) : ViewModel() {

    val email : MutableLiveData<String> = MutableLiveData("aa@aaa.com")
    val password : MutableLiveData<String> = MutableLiveData("12345678")

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

        var errorMessage = validators.validate()

        if(!errorMessage.isNullOrEmpty()) {
            alertViewModel.recordAlertMessage(errorMessage)
        } else {
            viewModelScope.launch {
                isLoading.value = true

                repository.login(loginEmail!!, loginPassword!!).addOnCompleteListener { task ->
                    isLoading.value = false
                    if(task.isSuccessful) {
                        alertViewModel.recordAlertMessage("Success!")
                    } else {
                        errorMessage = task.exception?.message ?: "Please check your login details"
                        alertViewModel.recordAlertMessage(errorMessage)
                    }
                }
            }
        }
    }

}
