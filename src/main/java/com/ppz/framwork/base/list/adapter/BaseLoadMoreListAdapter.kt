package com.ppz.framwork.base.list.adapter

import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.module.LoadMoreModule

abstract class BaseLoadMoreListAdapter<T>(@LayoutRes private val layoutResId: Int, data: MutableList<T>? = null) : BaseListAdapter<T>(layoutResId, data), LoadMoreModule