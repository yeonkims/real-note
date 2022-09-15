package com.yeonkims.realnoteapp.logic.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.yeonkims.realnoteapp.data.repositories.NoteRepository
import javax.inject.Inject

class NotesViewModel @Inject constructor(
    private val repository: NoteRepository
) {

    var noteList = listOf("BABY", "LOVE", "LIKE")

    var currentIndex : MutableLiveData<Int> =  MutableLiveData(0)

    var notePage : LiveData<String> = Transformations.map(currentIndex) { index ->
        return@map "${index + 1} / ${noteList.size}"
    }

    var nextIsEnabled : LiveData<Boolean> = Transformations.map(currentIndex) { index ->
        return@map index < noteList.size - 1
    }

    var prevIsEnabled : LiveData<Boolean> = Transformations.map(currentIndex) { index ->
        return@map index != 0
    }

    var currentNote = Transformations.map(currentIndex) { index ->
        if(noteList.isEmpty())
            return@map "You have no notes"
        else return@map noteList[index]
    }

    fun nextNote() {
        currentIndex.value = currentIndex.value?.plus(1)
    }

    fun prevNote() {
        currentIndex.value = currentIndex.value?.minus(1)
    }

}