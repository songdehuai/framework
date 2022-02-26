package com.ppz.framwork.base

import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.ppz.framwork.ext.autoNightMode
import com.ppz.framwork.App
import com.ppz.framwork.ext.toast
import com.ppz.framwork.ext.ui
import com.ppz.framwork.tools.StatusBarUtil
import com.ppz.framwork.tools.dialog.LoadDialog
import com.wcxk.framework.R
import kotlinx.coroutines.*

open class BaseActivity : AppCompatActivity(), CoroutineScope by CoroutineScope(Dispatchers.Main) {

    private val mainScope = MainScope()

    lateinit var thisActivity: BaseActivity

    private var loadingDialog: LoadDialog? = null

    private val mainColor = Color.parseColor("#FF6600")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        thisActivity = this
        StatusBarUtil.setLightMode(this)
        autoNightMode()
        initDialog()

    }

    private fun initDialog() {
        loadingDialog = LoadDialog(this)
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        autoPadding()
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        autoPadding()
    }

    fun autoPadding() {
        if (isAutoPadding()) {
            val titleView = findViewById<View>(R.id.cl_title_root)
            if (titleView != null) {
                titleView.setPadding(0, App.statusHeight, 0, 0)
            }
            val rootView = findViewById<View>(R.id.root_view)
            if (rootView != null) {
                rootView.setPadding(0, App.statusHeight, 0, 0)
            }
        }
    }

    open fun isAutoPadding(): Boolean {
        return true
    }

    open fun nightMode() {

    }

    open fun lightMode() {

    }

    open fun nightModeChanged(isNight: Boolean) {

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (autoNightMode()) {
            nightMode()
            nightModeChanged(true)
        } else {
            lightMode()
            nightModeChanged(false)
        }
    }

    fun showLoad(msg: String = "加载中") {
        ui { loadingDialog?.setMessage(msg)?.show() }
    }

    fun hideLoad() {
        ui { loadingDialog?.dismiss() }
    }


    override fun onDestroy() {
        super.onDestroy()
        hideLoad()
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