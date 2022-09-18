package com.yeonkims.realnoteapp.logic.viewmodels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeonkims.realnoteapp.data.repositories.NoteRepository
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class CreateNoteDialogViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val errorViewModel: ErrorViewModel
    ) : ViewModel() {

    var newNote: MutableLiveData<String> = MutableLiveData("")

    var createIsEnabled = Transformations.map(newNote) { content ->
        return@map !content.isNullOrEmpty()
    }

    fun createNote() {
        viewModelScope.launch {
            try {
                repository.createNote(newNote.value!!)
            } catch (e: Exception) {
                errorViewModel.recordErrorMessage(e.message)
            }
        }
    }

}