package com.yeonkims.realnoteapp.data.repositories

import androidx.lifecycle.LiveData
import com.yeonkims.realnoteapp.data.models.Note

interface NoteRepository {

    suspend fun fetchNotes()

    fun getNotes() : LiveData<List<Note>?>

    suspend fun deleteNote(id: Int)

    suspend fun createNote(content: String)

}