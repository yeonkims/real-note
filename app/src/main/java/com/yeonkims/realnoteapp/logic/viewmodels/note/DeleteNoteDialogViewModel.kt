package com.yeonkims.realnoteapp.logic.viewmodels.note


import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yeonkims.realnoteapp.data.models.Note
import com.yeonkims.realnoteapp.data.repositories.NoteRepository
import com.yeonkims.realnoteapp.logic.viewmodels.AlertViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class DeleteNoteDialogViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val alertViewModel: AlertViewModel
    ) : ViewModel() {


    fun deleteNote(note: Note) {
        val notes = repository.getNotes().value
        val selectedNote = if(note.id == null) {
            notes!!.last()
        } else {
            note
        }

        viewModelScope.launch {
            try {
                repository.deleteNote(selectedNote)
            } catch (e: Exception) {
                alertViewModel.recordAlertMessage(e.message)
            }
        }
    }

}