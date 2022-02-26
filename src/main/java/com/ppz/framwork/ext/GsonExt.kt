package com.ppz.framwork.ext

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


val gson = Gson()

inline fun <reified T> Any.toBean(): T {
    val type = object : TypeToken<T>() {}.type
    return gson.fromJson(this.toString(), type)
}


inline fun <reified T> String?.toBeanOrNull(): T? {
    if (this.isNullOrEmpty()) {
        return null
    }
    val type = object : TypeToken<T>() {}.type
    return gson.fromJson(this.toString(), type)
}

fun Any.toJson(): String {
    return gson.toJson(this)
}

fun Any.toArrayStr(): String {
    return gson.toJson(this).replace("\"", "")
}

fun Any.toArrayStrReplace(): String {
    return gson.toJson(this).replace("\"", "").replace("[", "").replace("]", "")
}


fun Any.convertToMap(): Map<String, String> {
    val json = this.toJson()
    return gson.fromJson(json, Map::class.java) as Map<String, String>
}
