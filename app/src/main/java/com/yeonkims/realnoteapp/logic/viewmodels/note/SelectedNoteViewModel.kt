package com.yeonkims.realnoteapp.logic.viewmodels.note

import androidx.lifecycle.*
import com.yeonkims.realnoteapp.data.models.Note
import com.yeonkims.realnoteapp.data.repositories.NoteRepository
import com.yeonkims.realnoteapp.data.repositories.UserRepository
import com.yeonkims.realnoteapp.logic.viewmodels.AlertViewModel
import com.yeonkims.realnoteapp.view.fragments.SelectedNoteFragmentArgs
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import java.util.*

class SelectedNoteViewModel @AssistedInject constructor(
    private val noteRepository: NoteRepository,
    private val userRepository: UserRepository,
    private val alertViewModel: AlertViewModel,
    @Assisted private val args: SelectedNoteFragmentArgs
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(args: SelectedNoteFragmentArgs): SelectedNoteViewModel
    }

    private val existingNote: Note? = args.selectedNote

    private val userId
        get() = userRepository.getCurrentUser().value!!.id

    private var isDeleted = false


    val currentNote: MutableLiveData<Note> = MutableLiveData(existingNote ?: Note.empty(userId))

    val isLoading: LiveData<Boolean> = Transformations.map(currentNote) {
        return@map it.id == null && (it.title.isNotEmpty() || it.content.isNotEmpty())
    }

    val title: MutableLiveData<String> = MutableLiveData(currentNote.value?.title)
    val content: MutableLiveData<String> = MutableLiveData(currentNote.value?.content)

    val latestNotes = noteRepository.getNotes()

    fun saveNote() {
        if(isLoading.value == true || isDeleted)
            return

        viewModelScope.launch {
            try {
                val initialNote = currentNote.value!!

                val noteToSave = initialNote.copy(
                    title = title.value!!,
                    content = content.value!!,
                    modifiedDate = Date(),
                )

                if(initialNote.hasIdenticalNoteData(noteToSave)) {
                    return@launch
                }

                val shouldCreateNewNote = noteToSave.id == null

                if(shouldCreateNewNote) {
                    noteRepository.createNote(noteToSave)
                } else {
                    noteRepository.updateNote(noteToSave)
                }

                currentNote.value = noteToSave

            } catch (e: Exception) {
                alertViewModel.recordAlertMessage(e.message)
            }
        }
    }

    fun deleteNote() {
        if(isLoading.value == true || isDeleted)
            return

        viewModelScope.launch {
            try {
                val noteToDelete = currentNote.value!!

                val shouldDeleteFromRepository = noteToDelete.id != null

                if(shouldDeleteFromRepository) {
                    noteRepository.deleteNote(noteToDelete)
                }

                isDeleted = true

            } catch (e: Exception) {
                alertViewModel.recordAlertMessage(e.message)
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            args: SelectedNoteFragmentArgs
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(args) as T
            }
        }
    }
}