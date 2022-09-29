package com.yeonkims.realnoteapp.logic.viewmodels.auth


import androidx.lifecycle.*
import com.yeonkims.realnoteapp.data.impl.temp_repositories.TempUserRepository
import com.yeonkims.realnoteapp.data.repositories.UserRepository
import com.yeonkims.realnoteapp.logic.viewmodels.AlertViewModel
import kotlinx.coroutines.launch
import com.yeonkims.realnoteapp.util.validators.*
import java.lang.Exception
import javax.inject.Inject

class ForgotPasswordDialogViewModel @Inject constructor(
    private val repository: UserRepository,
    private val alertViewModel: AlertViewModel,
    ) : ViewModel() {

    val email: MutableLiveData<String> = MutableLiveData("")

    val isValidEmail: LiveData<Boolean> = Transformations.map(email) {
        val validator = EmailValidator(email.value)
        val errorMessage = validator.validate()
        errorMessage.isNullOrEmpty()
    }

    fun sendResetLink() {
        val forgotPasswordEmail = email.value
        viewModelScope.launch {
            try {
                repository.resetPassword(forgotPasswordEmail!!) {
                    alertViewModel.recordAlertMessage(
                        "A password reset link was sent.\n" +
                                "Please check your email and reset your password.")
                }
            } catch (e: Exception) {
                alertViewModel.recordAlertMessage(e.message)
            }
        }
    }

}