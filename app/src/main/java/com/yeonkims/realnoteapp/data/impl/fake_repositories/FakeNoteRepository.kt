package com.yeonkims.realnoteapp.data.impl.fake_repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yeonkims.realnoteapp.data.models.Note
import com.yeonkims.realnoteapp.data.repositories.NoteRepository
import kotlinx.coroutines.delay
import java.net.HttpRetryException
import java.util.*

class FakeNoteRepository : NoteRepository {

    var savedNotes = mutableListOf(Note(1, "BABY"), Note(2, "LOVE"), Note(3, "LIKE"))
    var savedNotesLiveData = MutableLiveData<List<Note>>(null)

    var numberOfErrors = 3

    override suspend fun fetchNotes() {
        delay(5000L)
        if(numberOfErrors > 0){
            numberOfErrors--
            throw HttpRetryException("failed to load", 500)
        }
        savedNotesLiveData.postValue(savedNotes)
    }

    override fun getNotes(): LiveData<List<Note>?> {
        return savedNotesLiveData
    }

    override fun deleteNote(id: Int) {
        savedNotes = savedNotes.filter {  note ->
            return@filter note.id != id
        }.toMutableList()
        savedNotesLiveData.value = savedNotes
    }

    override fun createNote(content: String) {
        var nextId = 1
        if(savedNotes.isNotEmpty()) nextId = savedNotes.last().id + 1

        savedNotes.add(Note(nextId, content))
        savedNotesLiveData.value = savedNotes
    }
}