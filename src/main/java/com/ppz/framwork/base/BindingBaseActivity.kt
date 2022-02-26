package com.ppz.framwork.base

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BindingBaseActivity<Binding : ViewDataBinding>(private val view: Any? = null) : BaseActivity() {

    lateinit var binding: Binding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentViewDataBind(view)
        init(savedInstanceState)
    }

    open fun setContentViewDataBind(view: Any?) {
        when (view) {
            is Int -> {
                binding = DataBindingUtil.inflate<Binding>(layoutInflater, view, null, false)
                setContentView(binding.root)
            }
            is View -> {
                binding = DataBindingUtil.inflate<Binding>(layoutInflater, view.id, null, false)
                setContentView(binding.root)
            }
        }
    }

    abstract fun init(savedInstanceState: Bundle?)


}