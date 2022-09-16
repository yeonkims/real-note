package com.yeonkims.realnoteapp.logic.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.yeonkims.realnoteapp.data.models.Note
import com.yeonkims.realnoteapp.data.repositories.NoteRepository
import com.yeonkims.realnoteapp.util.livedata.PairLiveData
import com.yeonkims.realnoteapp.util.livedata.combine
import javax.inject.Inject

class NotesViewModel @Inject constructor(
    private val repository: NoteRepository
) {

    private var latestNotes: LiveData<List<Note>> = repository.getNotes()

    private var currentIndex : MutableLiveData<Int> =  MutableLiveData(0)

    private val combinedLiveData: PairLiveData<Int, List<Note>> = currentIndex.combine(latestNotes)

    var notePage : LiveData<String> = Transformations.map(combinedLiveData) { pair ->
        val index = pair.first!!
        val notes = pair.second!!

        return@map "${index + 1} / ${notes.size}"
    }

    var nextIsEnabled : LiveData<Boolean> = Transformations.map(combinedLiveData) { pair ->
        val index = pair.first!!
        val notes = pair.second!!

        return@map index < notes.size - 1
    }

    var prevIsEnabled : LiveData<Boolean> = Transformations.map(currentIndex) { index ->
        return@map index != 0
    }

    var hasNotes : LiveData<Boolean> = Transformations.map(latestNotes) { notes ->
        return@map notes.isNotEmpty()
    }

    var currentNote = Transformations.map(combinedLiveData) { pair ->
        val index = pair.first!!
        val notes = pair.second!!

        if(notes.isEmpty())
            return@map "You have no notes"
        else return@map notes[index].content
    }

    fun nextNote() {
        currentIndex.value = currentIndex.value?.plus(1)
    }

    fun prevNote() {
        currentIndex.value = currentIndex.value?.minus(1)
    }

    fun deleteNote() {
        val index = currentIndex.value!!
        val notes = latestNotes.value!!

        if(index == notes.size - 1 && index != 0)
            currentIndex.value = index.minus(1)
        repository.deleteNote(notes[index].id)
    }

}