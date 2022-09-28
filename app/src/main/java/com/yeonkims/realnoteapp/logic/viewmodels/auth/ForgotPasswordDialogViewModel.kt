package com.yeonkims.realnoteapp.logic.viewmodels.auth


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeonkims.realnoteapp.data.impl.temp_repositories.TempUserRepository
import com.yeonkims.realnoteapp.logic.viewmodels.AlertViewModel
import kotlinx.coroutines.launch
import com.yeonkims.realnoteapp.util.validators.*
import java.lang.Exception
import javax.inject.Inject

class ForgotPasswordDialogViewModel @Inject constructor(
    private val repository: TempUserRepository,
    private val alertViewModel: AlertViewModel,
    ) : ViewModel() {

    val email: MutableLiveData<String> = MutableLiveData("")

    fun isValidEmail() : Boolean {
        val validator = EmailValidator(email.value)
        val errorMessage = validator.validate()
        if(!errorMessage.isNullOrEmpty()) {
            alertViewModel.recordErrorMessage(errorMessage)
            return false
        }
        return true
    }

    fun sendResetLink() {
        val forgotPasswordEmail = email.value
        viewModelScope.launch {
            try {
                repository.resetPassword(forgotPasswordEmail!!)
            } catch (e: Exception) {
                alertViewModel.recordErrorMessage(e.message)
            }
        }
    }

}