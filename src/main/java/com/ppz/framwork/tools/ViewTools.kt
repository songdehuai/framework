package com.ppz.framwork.tools

import android.content.Context
import android.content.res.Resources
import android.view.*


object ViewTools {
    /**
     * 获取状态栏高度
     * @param context
     * @return
     */
    fun getStatusBarHeight(context: Context): Int {
        val resources: Resources = context.resources
        val resourceId: Int = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }

    /**
     * 获取是否有虚拟按键
     * 通过判断是否有物理返回键反向判断是否有虚拟按键
     * @param context
     * @return
     */
    fun checkDeviceHasNavigationBar(context: Context): Boolean {
        val hasMenuKey = ViewConfiguration.get(context)
            .hasPermanentMenuKey()
        val hasBackKey = KeyCharacterMap
            .deviceHasKey(KeyEvent.KEYCODE_BACK)
        return !hasMenuKey and !hasBackKey
    }
}