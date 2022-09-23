package com.yeonkims.realnoteapp.logic.viewmodels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeonkims.realnoteapp.data.models.Note
import com.yeonkims.realnoteapp.data.repositories.NoteRepository
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

class DeleteNoteDialogViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val errorViewModel: ErrorViewModel
    ) : ViewModel() {

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            try {
                repository.deleteNote(note)
            } catch (e: Exception) {
                errorViewModel.recordErrorMessage(e.message)
            }
        }
    }

}