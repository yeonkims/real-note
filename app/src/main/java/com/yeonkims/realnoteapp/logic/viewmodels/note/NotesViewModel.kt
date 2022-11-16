package com.yeonkims.realnoteapp.logic.viewmodels.note

import androidx.lifecycle.*
import com.yeonkims.realnoteapp.data.models.Note
import com.yeonkims.realnoteapp.data.repositories.NoteRepository
import com.yeonkims.realnoteapp.data.repositories.UserRepository
import com.yeonkims.realnoteapp.logic.viewmodels.AlertViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class NotesViewModel @Inject constructor(
    private val noteRepository: NoteRepository,
    private val userRepository: UserRepository,
    private val alertViewModel: AlertViewModel
) : ViewModel() {

    val currentUser = userRepository.getCurrentUser()

    var hasError : MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        viewModelScope.launch {

            try {
                noteRepository.fetchNotes(currentUser.value!!)
            } catch (e: Exception) {
                hasError.value = true
            }
        }

    }

    val latestNotes: LiveData<List<Note>?> = noteRepository.getNotes()

    var noteCount = Transformations.map(latestNotes) { notes ->
        if(notes == null) return@map "Connecting..."
        return@map "${notes?.size} ${if(notes?.size == 1) "Note" else "Notes"}"
    }

    var isLoading = Transformations.map(latestNotes) { notes ->
        return@map notes == null
    }

    var hasNotes : LiveData<Boolean> = Transformations.map(latestNotes) { notes ->
        return@map notes?.isNotEmpty() ?: false
    }

    fun selectedNote(index: Int) : Note {
        return latestNotes.value!![index]
    }

    fun retryFetch() {
        viewModelScope.launch {
            try {
                hasError.value = false
                noteRepository.fetchNotes(currentUser.value!!)
            } catch (e: Exception) {
                hasError.value = true
            }
        }
    }

}