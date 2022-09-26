package com.yeonkims.realnoteapp.data.repositories

import androidx.lifecycle.LiveData
import com.yeonkims.realnoteapp.data.models.Note

interface NoteRepository {

    suspend fun fetchNotes()

    fun getNotes() : LiveData<List<Note>?>

    suspend fun deleteNote(note: Note)

    suspend fun createNote(note: Note)

    suspend fun updateNote(note: Note)

}