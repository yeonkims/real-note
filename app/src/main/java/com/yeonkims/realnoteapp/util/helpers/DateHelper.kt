package com.yeonkims.realnoteapp.util.helpers

import java.text.SimpleDateFormat
import java.util.*

fun Date.format(pattern: String = "yyyy-MM-dd") : String {
    val simpleDateFormat = SimpleDateFormat(pattern)
    return simpleDateFormat.format(this)
}