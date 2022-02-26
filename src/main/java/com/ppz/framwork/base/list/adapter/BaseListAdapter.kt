package com.ppz.framwork.base.list.adapter

import androidx.annotation.LayoutRes
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

abstract class BaseListAdapter<T>(@LayoutRes private val layoutResId: Int, data: MutableList<T>? = null) : BaseQuickAdapter<T, BaseViewHolder>(layoutResId, data) {


}