package com.yeonkims.realnoteapp.data.repositories

import com.yeonkims.realnoteapp.data.models.Note

interface NoteRepository {

    fun getNotes() : List<Note>

    fun deleteNote(id: Int)

    fun createNote(content: String)

}