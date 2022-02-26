package com.ppz.framwork.ext

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions


fun ImageView.load(url: String) {
    try {
        url.isNullOrEmpty().ifFalse {
            Glide.with(this).load(url).into(this)
        }
    } catch (e: Exception) {
        e.message.logI()
    }
}


fun ImageView.load(resId: Int) {
    try {
        Glide.with(this).load(resId).into(this)
    } catch (e: Exception) {
        e.message.logI()
    }
}


fun ImageView.loadCircle(url: String) {
    try {
        url.isNullOrEmpty().ifFalse {
            Glide.with(this).load(url).apply(RequestOptions.bitmapTransform(CircleCrop())).into(this)
        }
    } catch (e: Exception) {
        e.message.logI()
    }
}
