package com.ppz.framwork.base.list.adapter

import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder

abstract class BaseBindingListAdapter<T, Binding : ViewDataBinding>(@LayoutRes private val layoutResId: Int, data: MutableList<T>? = null) : BaseQuickAdapter<T, BaseDataBindingHolder<Binding>>(layoutResId, data)
