package com.yeonkims.realnoteapp.logic.viewmodels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.yeonkims.realnoteapp.data.repositories.NoteRepository
import javax.inject.Inject

class CreateNoteDialogViewModel @Inject constructor(
    private val repository: NoteRepository
    ) {

    var newNote: MutableLiveData<String> = MutableLiveData("")

    var createIsEnabled = Transformations.map(newNote) { content ->
        return@map !content.isNullOrEmpty()
    }

    fun createNote() {
        repository.createNote(newNote.value!!)
    }

}