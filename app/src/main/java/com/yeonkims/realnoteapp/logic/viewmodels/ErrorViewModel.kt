package com.yeonkims.realnoteapp.logic.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorViewModel @Inject constructor(

) : ViewModel() {

    val snackbarMessage: MutableLiveData<String> = MutableLiveData()

    fun recordErrorMessage(errorMessage: String?) {
        snackbarMessage.value = errorMessage ?: "Failed to load."
    }

}