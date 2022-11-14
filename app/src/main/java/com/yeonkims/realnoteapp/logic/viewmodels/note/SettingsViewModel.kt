package com.yeonkims.realnoteapp.logic.viewmodels.note

import androidx.lifecycle.ViewModel
import com.yeonkims.realnoteapp.data.repositories.UserRepository
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val repository: UserRepository,
) : ViewModel() {

    val currentUser = repository.getCurrentUser()

}