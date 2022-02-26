package com.ppz.framwork.ext

import com.blankj.utilcode.util.LogUtils


fun Any?.logI() {
    this?.let {
        LogUtils.i(this)
    }
}

fun Any?.logD() {
    this?.let {
        LogUtils.d(this)
    }
}

fun Any?.logW() {
    this?.let {
        LogUtils.w(this)
    }
}

fun Any?.logE() {
    this?.let {
        LogUtils.e(this)
    }
}


fun String?.logJson() {
    this?.let {
        LogUtils.i(this.toJson())
    }
}