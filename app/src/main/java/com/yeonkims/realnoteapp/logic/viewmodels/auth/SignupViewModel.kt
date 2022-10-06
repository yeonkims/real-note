package com.yeonkims.realnoteapp.logic.viewmodels.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeonkims.realnoteapp.data.repositories.UserRepository
import com.yeonkims.realnoteapp.logic.viewmodels.AlertViewModel
import com.yeonkims.realnoteapp.util.dev_tools.Logger
import com.yeonkims.realnoteapp.util.validators.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignupViewModel @Inject constructor(
    private val repository: UserRepository,
    private val alertViewModel: AlertViewModel
) : ViewModel() {

    val email : MutableLiveData<String> = MutableLiveData("")
    val password : MutableLiveData<String> = MutableLiveData("")
    val passwordConfirm : MutableLiveData<String> = MutableLiveData("")

    val currentUser = repository.getCurrentUser()

    var isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    fun signUp() {
        if(isLoading.value == true) {
            return
        }

        val signupEmail = email.value
        val signupPassword = password.value
        val signupPasswordConfirm = passwordConfirm.value
        val signupFields = listOf(signupEmail, signupPassword, signupPasswordConfirm)

        val validators = listOf(
            NonEmptyFieldsValidator(signupFields),
            EmailValidator(signupEmail),
            PasswordValidator(signupPassword),
            MatchingPasswordsValidator(signupPassword, signupPasswordConfirm)
        )

        val errorMessage = validators.validate()

        if(!errorMessage.isNullOrEmpty()) {
            alertViewModel.recordAlertMessage(errorMessage)
        } else {
            viewModelScope.launch {
                isLoading.value = true

                repository.signUp(signupEmail!!, signupPassword!!) { isSuccess, errorMessage ->
                    isLoading.value = false

                    if(isSuccess) {
                        alertViewModel.recordAlertMessage("Success!")
                    } else {
                        Logger.i(errorMessage.toString())
                        alertViewModel.recordAlertMessage(errorMessage ?: "Please check your sign up details")
                    }
                }

            }
        }
    }

}