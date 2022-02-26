package com.ppz.framwork.ext

import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

private val handler = Handler(Looper.getMainLooper())
private val coreSize = Runtime.getRuntime().availableProcessors() + 1

private val http: ExecutorService = Executors.newFixedThreadPool(coreSize)
private val image: ExecutorService = Executors.newFixedThreadPool(3)
private val single: ExecutorService = Executors.newSingleThreadExecutor()

fun <T> T.ui(block: T.() -> Unit) {
    handler.post {
        try {
            block()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


fun <T> T.delay(delayMillis: Long, block: T.() -> Unit) {
    handler.postDelayed({
        try {
            block()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }, delayMillis)
}


fun <T> T.task(block: T.() -> Unit) {
    single.execute {
        try {
            block()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun <T> T.imageTask(block: T.() -> Unit) {
    image.execute {
        try {
            block()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}


fun <T> T.httpTask(block: T.() -> Unit) {
    http.execute {
        try {
            block()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun Int.startTimer(tick: (Int) -> Unit, finish: () -> Unit): CountDownTimer? {
    return object : CountDownTimer((this * 1000).toLong(), 1000) {
        override fun onTick(p0: Long) {
            tick((p0 / 1000).toInt())
        }

        override fun onFinish() {
            finish()
        }
    }.start()
}
