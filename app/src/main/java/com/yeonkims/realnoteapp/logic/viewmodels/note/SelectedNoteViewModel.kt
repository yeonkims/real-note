package com.yeonkims.realnoteapp.logic.viewmodels.note

import androidx.lifecycle.*
import com.yeonkims.realnoteapp.data.models.Note
import com.yeonkims.realnoteapp.data.repositories.NoteRepository
import com.yeonkims.realnoteapp.data.repositories.UserRepository
import com.yeonkims.realnoteapp.logic.viewmodels.AlertViewModel
import com.yeonkims.realnoteapp.util.helpers.format
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

    val note: Note? = args.selectedNote

    private val userId
        get() = userRepository.getCurrentUser().value!!.id

    val title: MutableLiveData<String> = MutableLiveData(note?.title ?: "")
    val content: MutableLiveData<String> = MutableLiveData(note?.content ?: "")
    val createdDate = (note?.createdDate ?: Date()).format()

    fun saveNote() {
        viewModelScope.launch {
            try {
                if(note == null) {
                    val newTitle = title.value!!
                    val newContent = content.value!!
                    if(!(newTitle.isEmpty() && newContent.isEmpty())) {
                        val newNote = Note.newNote(newTitle, newContent, userId)
                        noteRepository.createNote(newNote)
                    }
                } else {
                    noteRepository.updateNote(note.copy(id = note.id, title= title.value!!,content= content.value!!))
                }

            } catch (e: java.lang.Exception) {
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