package com.yeonkims.realnoteapp.logic.viewmodels.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeonkims.realnoteapp.data.impl.temp_repositories.TempUserRepository
import com.yeonkims.realnoteapp.logic.viewmodels.AlertViewModel
import com.yeonkims.realnoteapp.util.validators.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignupViewModel @Inject constructor(
    private val repository: TempUserRepository,
    private val alertViewModel: AlertViewModel
) : ViewModel() {

    val email : MutableLiveData<String> = MutableLiveData("")
    val password : MutableLiveData<String> = MutableLiveData("")
    val passwordConfirm : MutableLiveData<String> = MutableLiveData("")

    fun signUp() {

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
            alertViewModel.recordErrorMessage(errorMessage)
        } else {
            viewModelScope.launch {
                val isSuccess = repository.signUp(signupEmail!!, signupPassword!!)

                if(isSuccess) {
                    alertViewModel.recordErrorMessage("Success!")

                } else {
                    alertViewModel.recordErrorMessage("Please check your sign up details")
                }
            }
        }
    }

}