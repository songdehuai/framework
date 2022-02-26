package com.ppz.framwork.http.token

import com.tencent.mmkv.MMKV

object TokenManager {

    private const val MMKV_TOKEN = "MMKV_TOKEN"

    private const val MMKV_PUSH_TOKEN = "MMKV_PUSH_TOKEN"

    private const val LOGIN_FLAG = "LOGIN_FLAG"

    private val tokenMMKV by lazy { MMKV.mmkvWithID(MMKV_TOKEN) }

    fun getToken(): String {
        return tokenMMKV?.decodeString(MMKV_TOKEN, "") ?: ""
    }

    fun getPushToken(): String {
        return tokenMMKV?.decodeString(MMKV_PUSH_TOKEN, "") ?: ""
    }


    fun saveToken(token: String): Boolean? {
        return tokenMMKV?.encode(MMKV_TOKEN, token)
    }

    fun savePushToken(token: String): Boolean? {
        return tokenMMKV?.encode(MMKV_PUSH_TOKEN, token)
    }

    fun clearToken() {
        tokenMMKV?.clearAll()
    }

    fun isLogin(): Boolean {
        return tokenMMKV?.decodeBool(LOGIN_FLAG) ?: false
    }

    fun loginSuccess() {
        tokenMMKV?.encode(LOGIN_FLAG, true)
    }

    fun clearLoginFlag() {
        tokenMMKV?.remove(LOGIN_FLAG)
    }
}