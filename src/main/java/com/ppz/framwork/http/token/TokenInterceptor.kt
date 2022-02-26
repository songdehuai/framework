package com.ppz.framwork.http.token

import android.annotation.SuppressLint
import android.util.Log
import okhttp3.*


class TokenInterceptor : Interceptor {

    @SuppressLint("LongLogTag")
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = TokenManager.getToken()
        val pushToken = TokenManager.getPushToken()
        val request = chain.request().newBuilder()
            .addHeader("token", token)
            .build()
        Log.i("okhttp.OkHttpClient ", "token : ${token}\npushToken${pushToken}")
        return chain.proceed(request)
    }

}