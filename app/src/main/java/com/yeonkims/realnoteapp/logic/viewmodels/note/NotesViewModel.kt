package com.yeonkims.realnoteapp.logic.viewmodels.note

import androidx.lifecycle.*
import com.yeonkims.realnoteapp.data.models.Note
import com.yeonkims.realnoteapp.data.repositories.NoteRepository
import com.yeonkims.realnoteapp.logic.viewmodels.AlertViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

class NotesViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val alertViewModel: AlertViewModel
) : ViewModel() {

    var hasError : MutableLiveData<Boolean> = MutableLiveData(false)

    init {
        viewModelScope.launch {

            try {
                repository.fetchNotes()
            } catch (e: Exception) {
                hasError.value = true
            }
        }

    }

    val latestNotes: LiveData<List<Note>?> = repository.getNotes()

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
                repository.fetchNotes()
            } catch (e: Exception) {
                hasError.value = true
            }
        }
    }

}