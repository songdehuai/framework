package com.ppz.framwork.tools

import android.graphics.Bitmap
import com.blankj.utilcode.util.ImageUtils
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import java.security.MessageDigest

class BlurTransformation(val radius: Int = 90) : CenterCrop() {
    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val bitmap = super.transform(pool, toTransform, outWidth, outHeight)
        return ImageUtils.stackBlur(bitmap, radius)
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {}
}