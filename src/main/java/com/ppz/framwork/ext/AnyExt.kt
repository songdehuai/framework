package com.ppz.framwork.ext

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


fun Boolean.ifTrue(action: (Boolean) -> Unit): Boolean {
    if (this) {
        action.invoke(this)
    }
    return this
}


fun Boolean.ifFalse(action: (Boolean) -> Unit): Boolean {
    if (!this) {
        action.invoke(this)
    }
    return this
}


fun Boolean.trueIf(callback: () -> Unit) = apply {
    if (this) {
        callback.invoke()
    }
}

fun Boolean.falseIf(callback: () -> Unit) = apply {
    if (!this) {
        callback.invoke()
    }
}


@SuppressLint("SimpleDateFormat")
fun nowTime(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    return sdf.format(Date())
}


fun Int.success(action: (Boolean) -> Unit): Boolean {
    return (this == 200).also(action)
}


fun <T> Int.forArrayList(action: (Int) -> T): ArrayList<T> {
    val arrayList = arrayListOf<T>()
    for (i in 0..this) {
        arrayList.add(action.invoke(i))
    }
    return arrayList
}


inline fun <reified T> Int.forArray(action: (Int) -> T): Array<T?> {
    val array = arrayOfNulls<T>(this)
    for (i in 1..this) {
        array[i - 1] = action.invoke(i)
    }
    return array
}

fun <T> List<T>.takeRandom(count: Int): ArrayList<T> {
    val list = arrayListOf<T>()
    for (i in 1..count) {
        list.add(this.random())
    }
    return list
}