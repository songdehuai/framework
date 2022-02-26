package com.ppz.framwork.http

import com.ppz.framwork.http.token.TokenInvalidHandler
import com.google.gson.annotations.SerializedName


data class Result<T>(
    @SerializedName("code")
    var code: Int = 1,
    @SerializedName("msg")
    var message: String?,
    @SerializedName("data")
    var data: T?
) {

    fun isLogOut(): Boolean {
        return code == HttpResultCode.TOKEN_INVALID
    }

    fun isSuccess(successCode: Int = HttpResultCode.SUCCESS): Boolean {
        return code == successCode
    }

    fun isError(): Boolean {
        return code != HttpResultCode.SUCCESS
    }


    fun errorWith(block: (Result<T>) -> Unit) = apply {
        if (isLogOut()) {
            TokenInvalidHandler.error()
            return@apply
        }
        isError().also {
            if (it) {
                block(this)
            }
        }
    }


    fun error(block: (String) -> Unit) = apply {
        if (isLogOut()) {
            TokenInvalidHandler.error()
            return@apply
        }
        isError().also {
            if (it) {
                block(message ?: "服务器异常")
            }
        }
    }

    fun success(block: (T) -> Unit) = apply {
        error {}
        if (isSuccess()) block.invoke(data!!)
    }

    fun successWithCode(code: Int, block: (T) -> Unit) = apply {
        error {}
        if (isSuccess(code)) block.invoke(data!!)
    }


    fun successNull(block: () -> Unit) = apply {
        error {}
        if (isSuccess()) block.invoke()
    }


    fun result(block: (Result<T>) -> Unit) = apply {
        error { }
        block.invoke(this)
    }

}
