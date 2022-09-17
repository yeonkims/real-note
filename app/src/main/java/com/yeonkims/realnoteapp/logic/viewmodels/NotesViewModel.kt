package com.yeonkims.realnoteapp.logic.viewmodels

import androidx.lifecycle.*
import com.yeonkims.realnoteapp.data.models.Note
import com.yeonkims.realnoteapp.data.repositories.NoteRepository
import com.yeonkims.realnoteapp.util.livedata.PairLiveData
import com.yeonkims.realnoteapp.util.livedata.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

class NotesViewModel @Inject constructor(
    private val repository: NoteRepository
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

    private val latestNotes: LiveData<List<Note>?> = repository.getNotes()

    private var currentIndex : MutableLiveData<Int> =  MutableLiveData(0)

    var isLoading = Transformations.map(latestNotes) { notes ->
        return@map notes == null
    }

    private val combinedLiveData: PairLiveData<Int, List<Note>?> = currentIndex.combine(latestNotes)

    var notePage : LiveData<String> = Transformations.map(combinedLiveData) { pair ->
        val index = pair.first!!
        val notes = pair.second ?: emptyList<Note>()

        return@map "${index + 1} / ${notes.size}"
    }

    var nextIsEnabled : LiveData<Boolean> = Transformations.map(combinedLiveData) { pair ->
        val index = pair.first!!
        val notes = pair.second ?: emptyList<Note>()

        return@map index < notes.size - 1
    }

    var prevIsEnabled : LiveData<Boolean> = Transformations.map(currentIndex) { index ->
        return@map index != 0
    }

    var hasNotes : LiveData<Boolean> = Transformations.map(latestNotes) { notes ->
        return@map notes?.isNotEmpty() ?: false
    }

    var currentNote = Transformations.map(combinedLiveData) { pair ->
        val index = pair.first!!
        val notes = pair.second ?: emptyList()

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