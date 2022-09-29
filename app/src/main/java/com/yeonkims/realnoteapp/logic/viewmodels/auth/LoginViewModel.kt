package com.yeonkims.realnoteapp.logic.viewmodels.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeonkims.realnoteapp.data.impl.firebase_repositories.FirebaseUserRepository
import com.yeonkims.realnoteapp.data.impl.temp_repositories.TempUserRepository
import com.yeonkims.realnoteapp.data.repositories.UserRepository
import com.yeonkims.realnoteapp.logic.viewmodels.AlertViewModel
import com.yeonkims.realnoteapp.util.validators.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val repository: UserRepository,
    private val alertViewModel: AlertViewModel
) : ViewModel() {

    val email : MutableLiveData<String> = MutableLiveData("")
    val password : MutableLiveData<String> = MutableLiveData("")

    fun login() {
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
                repository.login(loginEmail!!, loginPassword!!) { isSuccess ->
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
