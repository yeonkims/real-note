package com.yeonkims.realnoteapp

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

class CreateNoteDialogViewModel {

    var newNote: MutableLiveData<String> = MutableLiveData("")

    var createIsEnabled = Transformations.map(newNote) { content ->
        return@map !content.isNullOrEmpty()
    }

    fun createNote() {
        Log.i("newNoteValue : ", "${newNote.value}")
    }

}