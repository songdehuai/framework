package com.ppz.framwork.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.ppz.framwork.App
import com.ppz.framework.R
import com.ppz.framwork.ext.toast
import kotlinx.coroutines.*


abstract class BindingBaseFragment<Binding : ViewDataBinding>(private val view: Int) : Fragment(), CoroutineScope by CoroutineScope(Dispatchers.Main) {


    private val mainScope = MainScope()

    lateinit var binding: Binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, view, container, false)
        if (isAutoPadding()) {
            initPadding()
        }
        init(savedInstanceState)
        return binding.root
    }

    private fun initPadding() {
        val rootView = binding.root.findViewById<View>(R.id.root_view)
        if (rootView != null) {
            rootView.setPadding(0, App.statusHeight, 0, 0)
        }
    }

    open fun isAutoPadding(): Boolean {
        return false
    }

    abstract fun init(savedInstanceState: Bundle?)


    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }


    fun launch(block: suspend () -> Unit): Job {
        return mainScope.launch {
            try {
                block()
            } catch (e: Exception) {
                e.message.toast()
                e.printStackTrace()
            }
        }
    }

    fun launch(block: suspend () -> Unit, error: () -> Unit): Job {
        return mainScope.launch {
            try {
                block()
            } catch (e: Exception) {
                error.invoke()
                e.message.toast()
                e.printStackTrace()
            }
        }
    }


    fun withLoading(block: suspend () -> Unit): Job {
        return launch {
            try {
                App.lifecycleCallbacks.showLoadDialog()
                block()
                App.lifecycleCallbacks.hideLoadDialog()
            } catch (ex: Exception) {
                ex.message.toast()
                App.lifecycleCallbacks.hideLoadDialog()
                ex.printStackTrace()
            }
        }
    }

    fun withLoading(block: suspend () -> Unit, error: () -> Unit): Job {
        return launch {
            try {
                App.lifecycleCallbacks.showLoadDialog()
                block()
                App.lifecycleCallbacks.hideLoadDialog()
            } catch (ex: Exception) {
                error.invoke()
                ex.message.toast()
                App.lifecycleCallbacks.hideLoadDialog()
                ex.printStackTrace()
            }
        }
    }

}