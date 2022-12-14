package com.yeonkims.realnoteapp.data.models

import android.os.Parcelable
import com.yeonkims.realnoteapp.util.extension_functions.format
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Note(
    val id: Int?,
    val title: String,
    val content: String,
    val createdDate: Date,
    val modifiedDate: Date,
    val userId: String,
) : Parcelable {

    val modifiedDateString: String
        get() = modifiedDate.format()

    fun hasIdenticalNoteData(otherNote: Note) : Boolean {
        return title == otherNote.title &&  content == otherNote.content && createdDate == otherNote.createdDate
    }

    override fun toString(): String = id.toString()

    companion object {
        fun empty(userId: String) : Note {
            return Note(
                id = null,
                title = "",
                content = "",
                createdDate = Date(),
                modifiedDate = Date(),
                userId= userId,
            )
        }
        fun newNote(newTitle: String, newContent: String, userId: String) : Note {
            return Note(
                id = null,
                title = newTitle,
                content = newContent,
                createdDate = Date(),
                modifiedDate = Date(),
                userId = userId
            )
        }
    }
}