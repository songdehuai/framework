package com.ppz.framwork.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ppz.framwork.App
import com.ppz.framwork.ext.toast
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


open class BaseViewModel : ViewModel() {

    fun launch(block: suspend () -> Unit): Job {
        return viewModelScope.launch {
            try {
                block()
            } catch (e: Exception) {
                "服务器异常".toast()
                e.printStackTrace()
            }
        }
    }


    fun launch(block: suspend () -> Unit, error: () -> Unit): Job {
        return viewModelScope.launch {
            try {
                block()
            } catch (e: Exception) {
                error.invoke()
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

    fun showLoad() {
        App.lifecycleCallbacks.showLoadDialog()
    }

    fun hideLoad() {
        App.lifecycleCallbacks.hideLoadDialog()
    }

    fun withLoading(block: suspend () -> Unit, error: () -> Unit): Job {
        return launch {
            try {
                App.lifecycleCallbacks.showLoadDialog()
                block()
                App.lifecycleCallbacks.hideLoadDialog()
            } catch (ex: Exception) {
                error.invoke()
                App.lifecycleCallbacks.hideLoadDialog()
                ex.printStackTrace()
            }
        }
    }

}