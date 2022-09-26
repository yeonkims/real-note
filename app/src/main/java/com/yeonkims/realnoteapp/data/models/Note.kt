package com.yeonkims.realnoteapp.data.models

import android.os.Parcelable
import com.yeonkims.realnoteapp.util.helpers.format
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class Note(
    val id: Int?,
    val title: String,
    val content: String,
    val createdDate: Date,
    val modifiedDate: Date?,
) : Parcelable {

    val createdDateString: String
        get() = createdDate.format()

    override fun toString(): String = id.toString()

    companion object {
        fun newNote(newTitle: String, newContent: String) : Note {
            return Note(
                id = null,
                title = newTitle,
                content = newContent,
                createdDate = Date(),
                modifiedDate = null
            )
        }
    }
}