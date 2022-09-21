package com.yeonkims.realnoteapp.util.helpers

import com.google.firebase.functions.HttpsCallableResult
import com.google.gson.Gson

inline fun <reified T> Gson.parseData(result : HttpsCallableResult) : T {
     val data = result.data
    val dataJson = (data as HashMap<*, *>)["res"]

    return fromJson(
        toJson(dataJson),
        T::class.java
    )
}
inline fun <reified T> Gson.parseList(result : HttpsCallableResult) : List<T> {
    val data = result.data
    val listJson = (data as HashMap<*, *>)["res"]

    return fromJson(
        toJson(listJson),
        Array<T>::class.java
    ).toList()
}