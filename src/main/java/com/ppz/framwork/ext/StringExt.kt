package com.ppz.framwork.ext

import android.text.*
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import com.tencent.mmkv.MMKV
import java.util.*


fun String.getColorText(color: Int): CharSequence {
    val style = SpannableStringBuilder(this)
    style.setSpan(ForegroundColorSpan(color), 0, this.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return style
}


fun String.getSizeText(dp: Int): CharSequence {
    val ss = SpannableString(this)
    val ass = AbsoluteSizeSpan(dp, true)
    ss.setSpan(ass, 0, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
    return SpannedString(ss)
}

fun String.toSafePhone(): String {
    if (this.isNullOrEmpty()) {
        return "***********"
    }
    if (this.length >= 7) {
        val temp = substring(3, 7)
        val newPhone = this.replaceFirst(temp, "****")
        return newPhone
    }
    return this
}


private val mmkv by lazy { MMKV.mmkvWithID("EXT") }


fun save(key: String, value: String) {
    mmkv?.putString(key, value)
}

fun getSave(key: String, default: String): String {
    return mmkv?.getString(key, default)!!
}


fun String.isImg(): Boolean {
    return this
        .endsWith(".png", true) or this
        .endsWith(".jpg", true) or this
        .endsWith(".bmp", true) or this
        .endsWith(".gif", true) or this
        .endsWith(".jpeg", true) or this
        .endsWith(".webp", true)
}


fun getUUIDStr(): String {
    return UUID.randomUUID().toString().toUpperCase(Locale.ROOT).replace("-", "")
}