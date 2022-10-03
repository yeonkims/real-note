package com.yeonkims.realnoteapp.util.dev_tools

import android.util.Log

object Logger {
    private val TAG = "REAL-NOTE-LOG"

    fun i(msg: String) = Log.i(TAG, msg)
}