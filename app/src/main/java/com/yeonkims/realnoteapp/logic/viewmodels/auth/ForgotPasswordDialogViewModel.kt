package com.yeonkims.realnoteapp.logic.viewmodels.auth


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeonkims.realnoteapp.data.impl.temp_repositories.TempUserRepository
import com.yeonkims.realnoteapp.logic.viewmodels.ErrorViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class ForgotPasswordDialogViewModel @Inject constructor(
    private val repository: TempUserRepository,
    private val errorViewModel: ErrorViewModel
    ) : ViewModel() {

    val email: MutableLiveData<String> = MutableLiveData("")

    fun checkValidEmail() : Boolean {
        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(email.value).matches()) {
            errorViewModel.recordErrorMessage("Please enter valid email")
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
                errorViewModel.recordErrorMessage(e.message)
            }
        }
    }

}