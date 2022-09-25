package com.yeonkims.realnoteapp.data.models

import android.os.Parcelable
import com.yeonkims.realnoteapp.util.helpers.format
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*

@Parcelize
data class Note (
    val id: Int,
    val title: String,
    val content: String,
    val createdDate: Date,
) : Parcelable {

    val createdDateString: String
        get() = createdDate.format()

    override fun toString(): String = id.toString()
}