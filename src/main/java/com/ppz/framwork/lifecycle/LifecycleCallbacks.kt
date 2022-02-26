package com.ppz.framwork.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.ppz.framwork.base.BaseActivity
import com.ppz.framwork.tools.StatusBarUtil

/**
 * Activity生命周期管理
 */
class LifecycleCallbacks : Application.ActivityLifecycleCallbacks {

    val activityList = arrayListOf<Activity>()


    private var decorView: View? = null
    private var controller: WindowInsetsControllerCompat? = null

    fun getDecorView(): View? {
        return decorView
    }

    fun getController(): WindowInsetsControllerCompat? {
        return controller
    }


    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activityList.add(activity)
        decorView = activity.window.decorView
        decorView?.let {
            controller = WindowCompat.getInsetsController(activity.window, it)
        }
        if (activity.localClassName == "com.alibaba.sdk.android.feedback.windvane.CustomHybirdActivity") {
            StatusBarUtil.setLightMode(activity)
        }
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {
        if (activity.isFinishing) {
            activityList.remove(activity)
            hideLoadDialog(activity)
        }
    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        hideLoadDialog(activity)
        if (activityList.isEmpty()) {
            decorView = null
            controller = null
        }
    }

    fun isVisible(type: Int): Boolean {
        return ViewCompat.getRootWindowInsets(decorView!!)!!.isVisible(type)
    }


    fun showLoadDialog() {
        if (activityList.last() is BaseActivity) {
            (activityList.last() as BaseActivity).showLoad()
        }
    }

    fun hideLoadDialog(activity: Activity? = activityList.last()) {
        if (activity is BaseActivity) {
            activity.hideLoad()
        }
    }

}