package com.ppz.framwork.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Parcelable
import android.view.Gravity
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.blankj.utilcode.util.ServiceUtils
import com.bumptech.glide.Glide
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.engine.ImageEngine
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnImageCompleteCallback
import com.luck.picture.lib.listener.OnResultCallbackListener
import com.luck.picture.lib.widget.longimage.SubsamplingScaleImageView
import com.ppz.framework.R
import com.ppz.framwork.base.BaseActivity
import com.ppz.framwork.tools.StatusBarUtil


fun Intent.getString(key: String, default: String = ""): String {
    return if (this.hasExtra(key)) {
        this.getStringExtra(key) ?: ""
    } else {
        default
    }
}

fun <T : Parcelable> Intent.check(key: String): T? {
    return if (this.hasExtra(key)) {
        this.getParcelableExtra<T>(key)
    } else {
        null
    }
}

fun <T : Parcelable> Intent.checkList(key: String): ArrayList<T>? {
    return if (this.hasExtra(key)) {
        this.getParcelableArrayListExtra<T>(key)
    } else {
        null
    }
}


fun Context.start(cls: Class<*>) {
    val intent = Intent(this, cls)
    this.startActivity(intent)
}


fun Context.startService(cls: Class<*>) {
    val intent = Intent(this, cls)
    ServiceUtils.startService(intent)
}

fun Context.stopService(cls: Class<*>) {
    try {
        val intent = Intent(this, cls)
        this.stopService(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.startWithAnim(cls: Class<*>, animIn: Int, animOut: Int) {
    this.start(cls)
    if (this is Activity) {
        this.overridePendingTransition(animIn, animOut)
    }
}


fun Context.startNewTask(cls: Class<*>) {
    val intent = Intent(this, cls)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    this.startActivity(intent)
}

fun Fragment.startNewTask(cls: Class<*>) {
    val intent = Intent(this.activity, cls)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    this.startActivity(intent)
}


fun Fragment.start(cls: Class<*>) {
    val intent = Intent(this.context, cls)
    this.startActivity(intent)
}

/**
 * 是否开启暗黑模式
 */
fun Activity.isNightMode(): Boolean {
    val currentNightMode: Int = this.resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK
    return currentNightMode == Configuration.UI_MODE_NIGHT_YES
}

fun Activity.autoNightMode(): Boolean {
    return this.isNightMode().also { night ->
        if (night) {
            this.setDarkMode()
            if (this is BaseActivity) {
                this.nightMode()
            }
        } else {
            this.setLightMode()
            if (this is BaseActivity) {
                this.lightMode()
            }
        }
    }
}

fun Activity.translucent() {
    StatusBarUtil.setTranslucent(this)
}

fun Activity.setLightMode() {
    StatusBarUtil.setLightMode(this)
}

fun Activity.setDarkMode() {
    StatusBarUtil.setDarkMode(this)
}

@RequiresApi(Build.VERSION_CODES.R)
fun Activity.dialogActivityMargin(margin: Float) {
    val params = this.window.attributes
    params.width = this.windowManager.currentWindowMetrics.bounds.right - margin.dp2px(this)
    params.gravity = Gravity.CENTER
    window.setBackgroundDrawableResource(R.color.white)
    window.attributes = params
}

fun Activity.selectImage(success: (List<LocalMedia?>) -> Unit) {
    PictureSelector.create(this)
        .openGallery(PictureMimeType.ofImage())
        .maxSelectNum(1)
        .selectionMode(PictureConfig.SINGLE)
        .isSingleDirectReturn(true)
        .setPictureWindowAnimationStyle(null)
        .imageEngine(object : ImageEngine {
            override fun loadImage(context: Context, url: String, imageView: ImageView) {
                Glide.with(context).load(url).into(imageView)
            }

            override fun loadImage(context: Context, url: String, imageView: ImageView, longImageView: SubsamplingScaleImageView?, callback: OnImageCompleteCallback?) {
                Glide.with(context).load(url).into(imageView)
            }

            override fun loadFolderImage(context: Context, url: String, imageView: ImageView) {
                Glide.with(context).load(url).into(imageView)
            }

            override fun loadGridImage(context: Context, url: String, imageView: ImageView) {
                Glide.with(context).load(url).into(imageView)
            }

        })
        .forResult(object : OnResultCallbackListener<LocalMedia?> {
            override fun onResult(result: List<LocalMedia?>) {
                success(result)
            }

            override fun onCancel() {

            }
        })
}
