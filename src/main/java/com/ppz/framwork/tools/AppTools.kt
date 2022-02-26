package com.ppz.framwork.tools

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import com.ppz.framwork.App
import kotlin.system.exitProcess

object AppTools {
    /**
     * 重启APP
     */
    fun reStartApp() {
        val intent = App.app.packageManager
            .getLaunchIntentForPackage(App.app.packageName)
        val restartIntent = PendingIntent.getActivity(App.app, 0, intent, 0)
        val mgr = App.app.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        mgr[AlarmManager.RTC, System.currentTimeMillis() + 1000] = restartIntent
        exitProcess(0)
    }

}