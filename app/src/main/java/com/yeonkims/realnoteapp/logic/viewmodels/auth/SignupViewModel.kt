package com.yeonkims.realnoteapp.logic.viewmodels.auth

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeonkims.realnoteapp.data.impl.temp_repositories.TempUserRepository
import com.yeonkims.realnoteapp.logic.viewmodels.ErrorViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class SignupViewModel @Inject constructor(
    private val repository: TempUserRepository,
    private val errorViewModel: ErrorViewModel
) : ViewModel() {

    val email : MutableLiveData<String> = MutableLiveData("")
    val password : MutableLiveData<String> = MutableLiveData("")
    val passwordConfirm : MutableLiveData<String> = MutableLiveData("")

    fun signUp() {

        val signupEmail = email.value
        val signupPassword = password.value
        val signupPasswordConfirm = passwordConfirm.value

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(signupEmail).matches()) {
            errorViewModel.recordErrorMessage("Please enter valid email")
        }

        if(signupPassword.isNullOrEmpty() || signupPasswordConfirm.isNullOrEmpty()) {
            errorViewModel.recordErrorMessage("Please fill in all the blanks")
        }

        if (signupPassword != signupPasswordConfirm) {
            errorViewModel.recordErrorMessage("Passwords do not match")
        }

        viewModelScope.launch {
            val isSuccess = repository.signUp(signupEmail!!, signupPassword!!)

        }
    }

}