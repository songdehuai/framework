package com.ppz.framwork.ext

import android.graphics.Color
import android.view.Gravity
import com.blankj.utilcode.util.ToastUtils
import com.ppz.framework.R
import com.ppz.framwork.global.TipType


fun Any?.toast() {
    this.tip(TipType.SUCCESS)
}


fun Any?.tipSuccess() {
    this.tip(TipType.SUCCESS)
}

fun Any?.tipError() {
    this.tip(TipType.ERROR)
}


fun Any?.tipWarning() {
    this.tip(TipType.WARNING)
}


fun Any?.tipWtf() {
    this.tip(TipType.WTF)
}


fun Any?.tip(type: TipType, isIcon: Boolean = false) {
    if (this.toString().isEmpty()) {
        return
    }
    var color = successColor
    var topIcon = R.drawable.ic_success

    when (type) {
        TipType.SUCCESS -> {
            color = successColor
            topIcon = R.drawable.ic_success
        }
        TipType.ERROR -> {
            color = errorColor
            topIcon = R.drawable.ic_error
        }
        TipType.WARNING -> {
            color = warningColor
            topIcon = R.drawable.ic_warning
        }
        TipType.WTF -> {
            color = wtfColor
            topIcon = R.drawable.ic_wtf
        }
    }
    if (isIcon) {
        ToastUtils.make().setBgColor(color).setTopIcon(topIcon).setTextColor(Color.WHITE).show(this.toString())
    } else {
        ToastUtils.make().setBgColor(color).setTextColor(Color.WHITE).show(this.toString())
    }

}




