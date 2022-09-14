package com.yeonkims.realnoteapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

class NotesViewModel {

    var noteList = listOf<String>("BABY", "LOVE", "LIFE")

    var currentIndex : MutableLiveData<Int> =  MutableLiveData(0)

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