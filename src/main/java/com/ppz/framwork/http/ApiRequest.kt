package com.ppz.framwork.http

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.hjq.gson.factory.GsonFactory
import com.ppz.framework.BuildConfig
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


inline fun <reified T> apiCreator(baseUrl: String): T {
    ApiRequest.baseUrl = baseUrl
    return if (ApiRequest.apiRequestPool.containsKey(T::class.java.name)) {
        ApiRequest.apiRequestPool[T::class.java.name] as T
    } else {
        ApiRequest.retrofit.create(T::class.java).also {
            ApiRequest.apiRequestPool[T::class.java.name] = it!!
        }
    }
}

fun okhttpClient(): OkHttpClient {
    return ApiRequest.client
}

object ApiRequest {

    val apiRequestPool = hashMapOf<String, Any>()

    var baseUrl = ""

    private val log by lazy {
        HttpLoggingInterceptor().also { it.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE }
    }

    val client = OkHttpClient
        .Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(log)
        .retryOnConnectionFailure(true)
        .build()


    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonFactory.getSingletonGson()))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
    }


}