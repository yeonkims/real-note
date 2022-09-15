package com.yeonkims.realnoteapp.data.impl.fake_repositories

import android.os.Build
import androidx.annotation.RequiresApi
import com.yeonkims.realnoteapp.data.models.Note
import com.yeonkims.realnoteapp.data.repositories.NoteRepository

class FakeNoteRepository : NoteRepository {

    var savedNotes = mutableListOf(Note(1, "BABY"), Note(2, "LOVE"), Note(3, "LIKE"))

    override fun getNotes(): List<Note> {
        return savedNotes
    }

    override fun deleteNote(id: Int) {
        savedNotes = savedNotes.filter {  note ->
            return@filter note.id == id
        }.toMutableList()
    }

    override fun createNote(content: String) {
        var nextId = savedNotes.last().id + 1
        savedNotes.add(Note(nextId, content))
    }
}