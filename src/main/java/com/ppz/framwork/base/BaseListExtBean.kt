package com.ppz.framwork.base


import com.google.gson.annotations.SerializedName


data class BaseListExtBean<T, E>(
    @SerializedName("itemList")
    val itemList: List<T>?,
    @SerializedName("page")
    val page: Int = 0,
    @SerializedName("pageSize")
    val pageSize: Int = 0,
    @SerializedName("totalCount")
    val totalCount: Int = 0,
    @SerializedName("hasMore")
    val hasMore: Boolean = false,
    @SerializedName("ext")
    val ext: E? = null
)
