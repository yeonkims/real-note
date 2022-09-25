package com.yeonkims.realnoteapp.logic.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

class ErrorViewModel @Inject constructor(

) : ViewModel() {

    var snackbarMessage: MutableLiveData<String> = MutableLiveData()

    fun recordErrorMessage(errorMessage: String?) {
        snackbarMessage.value = errorMessage ?: "Failed to load."
    }

}