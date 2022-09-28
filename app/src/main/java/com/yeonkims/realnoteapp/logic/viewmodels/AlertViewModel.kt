package com.yeonkims.realnoteapp.logic.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlertViewModel @Inject constructor(

) : ViewModel() {

    val snackbarMessage: MutableLiveData<String> = MutableLiveData()

    fun recordAlertMessage(alertMessage: String?) {
        snackbarMessage.value = alertMessage ?: "Failed to load."
    }

}