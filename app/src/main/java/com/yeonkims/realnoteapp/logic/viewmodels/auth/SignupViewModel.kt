package com.yeonkims.realnoteapp.logic.viewmodels.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.yeonkims.realnoteapp.data.impl.temp_repositories.TempUserRepository
import com.yeonkims.realnoteapp.data.repositories.UserRepository
import com.yeonkims.realnoteapp.logic.viewmodels.AlertViewModel
import com.yeonkims.realnoteapp.util.validators.*
import com.yeonkims.realnoteapp.view.fragments.SignupFragmentDirections
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

                repository.signUp(signupEmail!!, signupPassword!!) { isSuccess ->
                    isLoading.value = false

                    if(isSuccess) {
                        alertViewModel.recordAlertMessage("Success!")
                    } else {
                        alertViewModel.recordAlertMessage("Please check your sign up details")
                    }
                }

            }
        }
    }

}