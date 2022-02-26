package com.ppz.framwork.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ppz.framwork.App
import com.ppz.framwork.ext.toast
import kotlinx.coroutines.*

abstract class BaseFragment : Fragment(), CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private val mainScope = MainScope()


    lateinit var mView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getLayout(container).let {
            if (it is View) {
                mView = it
            }
            if (it is Int) {
                mView = View.inflate(context, it, null)
            }
        }
        init(savedInstanceState)
        return mView
    }

    abstract fun getLayout(container: ViewGroup?): Any


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
                "服务器异常".toast()
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
                "服务器异常".toast()
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
                "服务器异常".toast()
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
                "服务器异常".toast()
                App.lifecycleCallbacks.hideLoadDialog()
                ex.printStackTrace()
            }
        }
    }
}