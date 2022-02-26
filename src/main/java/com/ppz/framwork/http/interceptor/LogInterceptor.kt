package com.ppz.framwork.http.interceptor

import com.ppz.framwork.ext.logI
import com.ppz.framework.BuildConfig

import okhttp3.logging.HttpLoggingInterceptor
import java.io.UnsupportedEncodingException
import java.net.URLDecoder


object LogInterceptor {

    var interceptor = HttpLoggingInterceptor { message ->
        try {
            val text: String = URLDecoder.decode(message, "utf-8")
            "OKHttp-----${text}".logI()
        } catch (e: UnsupportedEncodingException) {
            "OKHttp-----${message}".logI()
            e.printStackTrace()
        }
    }.also { it.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE }

}