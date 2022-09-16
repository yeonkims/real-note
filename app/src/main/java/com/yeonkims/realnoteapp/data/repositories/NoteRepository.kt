package com.yeonkims.realnoteapp.data.repositories

import androidx.lifecycle.LiveData
import com.yeonkims.realnoteapp.data.models.Note

interface NoteRepository {

    fun getNotes() : LiveData<List<Note>>

    fun deleteNote(id: Int)

    fun createNote(content: String)

}