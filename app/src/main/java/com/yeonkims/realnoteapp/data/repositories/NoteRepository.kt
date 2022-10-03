package com.yeonkims.realnoteapp.data.repositories

import androidx.lifecycle.LiveData
import com.yeonkims.realnoteapp.data.models.Note
import com.yeonkims.realnoteapp.data.models.User

interface NoteRepository {

    suspend fun fetchNotes(user: User)

    fun getNotes() : LiveData<List<Note>?>

    suspend fun deleteNote(note: Note)

    suspend fun createNote(note: Note)

    suspend fun updateNote(note: Note)

}