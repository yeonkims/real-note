package com.yeonkims.realnoteapp.logic.viewmodels

import androidx.lifecycle.*
import com.yeonkims.realnoteapp.data.models.Note
import com.yeonkims.realnoteapp.data.repositories.NoteRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class SelectedNoteViewModel @Inject constructor(
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