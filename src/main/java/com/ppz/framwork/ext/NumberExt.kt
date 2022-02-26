package com.ppz.framwork.ext

import android.content.Context
import com.blankj.utilcode.util.ScreenUtils
import java.lang.IllegalArgumentException
import java.math.BigDecimal
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun getWidth(): Int {
    return ScreenUtils.getScreenWidth()
}

fun getHeight(): Int {
    return ScreenUtils.getScreenHeight()
}

/**
 * 将px值转换为dip或dp值，保证尺寸大小不变
 */
fun Float.px2dp(context: Context): Int {
    val scale = context.resources.displayMetrics.density
    return (this / scale + 0.5f).toInt()
}


/**
 * 将dip或dp值转换为px值，保证尺寸大小不变
 */
fun Int.dp2px(context: Context): Int {
    val scale = context.resources.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

/**
 * 将dip或dp值转换为px值，保证尺寸大小不变
 */
fun Float.dp2px(context: Context): Int {
    val scale = context.resources.displayMetrics.density
    return (this * scale + 0.5f).toInt()
}

/**
 * 将px值转换为sp值，保证文字大小不变
 */
fun Float.px2sp(context: Context): Int {
    val fontScale = context.resources.displayMetrics.scaledDensity
    return (this / fontScale + 0.5f).toInt()
}

/**
 * 将sp值转换为px值，保证文字大小不变
 */
fun Float.sp2px(context: Context): Int {
    val fontScale = context.resources.displayMetrics.scaledDensity
    return (this * fontScale + 0.5f).toInt()
}

fun Float.m2kmUp(): BigDecimal {
    return this.toBigDecimal().divide(1000.toBigDecimal()).setScale(1, BigDecimal.ROUND_UP)
}

fun Int.m2kmUp(): BigDecimal {
    return this.toBigDecimal().divide(1000.toBigDecimal()).setScale(1, BigDecimal.ROUND_UP)
}

fun Float.km2mUp(): Float {
    return this.toBigDecimal().km2mUp().toFloat()
}

fun BigDecimal.km2mUp(): BigDecimal {
    return this.multiply(1000.toBigDecimal()).setScale(1, BigDecimal.ROUND_UP)
}

fun Float.km2mDown(): Float {
    return this.toBigDecimal().km2mDown().toFloat()
}

fun BigDecimal.km2mDown(): BigDecimal {
    return this.multiply(1000.toBigDecimal()).setScale(1, BigDecimal.ROUND_DOWN)
}

fun Float.m2sUp(): BigDecimal {
    return this.toBigDecimal().multiply(60.toBigDecimal()).toInt().toBigDecimal()
}

fun Int.s2mUpUp(): BigDecimal {
    return this.toBigDecimal().divide(60.toBigDecimal(), BigDecimal.ROUND_UP)
}

fun Int.s2mUpDown(): BigDecimal {
    return this.toBigDecimal().divide(60.toBigDecimal(), BigDecimal.ROUND_DOWN)
}


fun isInTime(sourceTime: String?, curTime: String?): Boolean {
    require(!(sourceTime == null || !sourceTime.contains("-") || !sourceTime.contains(":"))) { "Illegal Argument arg:$sourceTime" }
    require(!(curTime == null || !curTime.contains(":"))) { "Illegal Argument arg:$curTime" }
    val args = sourceTime.split("-").toTypedArray()
    val sdf = SimpleDateFormat("HH:mm")
    return try {
        val now = sdf.parse(curTime).time
        val start = sdf.parse(args[0]).time
        val end = sdf.parse(args[1]).time
        if (args[1] == "00:00") {
            args[1] = "24:00"
        }
        if (end < start) {
            now !in end until start
        } else {
            now in start until end
        }
    } catch (e: ParseException) {
        e.printStackTrace()
        throw IllegalArgumentException("Illegal Argument arg:$sourceTime")
    }
}


fun getNowTimeSpan(): String {
    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val minute = Calendar.getInstance().get(Calendar.MINUTE)
    return "${hour}:${minute}"
}