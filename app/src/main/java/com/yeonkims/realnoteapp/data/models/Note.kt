package com.yeonkims.realnoteapp.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Note (
    val id: Int,
    val title: String,
    val content: String,
    val created_date: String,
) : Parcelable