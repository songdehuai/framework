package com.ppz.framwork

import android.app.Application
import com.blankj.utilcode.util.*
import com.ppz.framework.BuildConfig
import com.ppz.framwork.http.token.TokenInvalidHandler
import com.ppz.framwork.lifecycle.LifecycleCallbacks

import com.ppz.framwork.tools.ViewTools
import com.ppz.framwork.http.token.TokenManager
import com.tencent.mmkv.MMKV
import com.zxy.recovery.core.Recovery
import java.io.File


open class App : Application() {

    companion object {

        lateinit var app: App

        val statusHeight by lazy { ViewTools.getStatusBarHeight(app) }

        val lifecycleCallbacks = LifecycleCallbacks()
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        init()
    }


    private fun init() {
        MMKV.initialize(this)
        Utils.init(this)
        LogUtils.getConfig().isLogSwitch = BuildConfig.DEBUG
        registerActivityLifecycleCallbacks(lifecycleCallbacks)
        TokenInvalidHandler.handler {
            TokenManager.clearToken()
            TokenManager.clearLoginFlag()
        }
        initCrash()
        LogUtils.getConfig().saveDays = 3
        LogUtils.getConfig().setFileWriter { file, content ->
            File(file).writeText(content)
        }
    }

    private fun initCrash() {
        Recovery.getInstance()
            .debug(BuildConfig.DEBUG)
            .recoverInBackground(false)
            .recoverStack(true)
            .recoverEnabled(true)
            .silent(false, Recovery.SilentMode.RECOVER_ACTIVITY_STACK)
            .init(this)
    }
}