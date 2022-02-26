package com.ppz.framwork.ext

import android.app.AlertDialog
import android.view.Gravity
import androidx.appcompat.app.AppCompatActivity
import com.ppz.framwork.tools.dialog.LoadDialog

fun androidx.appcompat.app.AlertDialog.Builder.fixShow(width: Float = 0.95f) = apply {
    this.create().fixShow(width)
}

fun AlertDialog.Builder.fixShow(width: Float = 0.95f) = apply {
    this.create().fixShow(width)
}


fun androidx.appcompat.app.AlertDialog.fixShow(width: Float = 0.95f) = apply {
    this.show()
    this.window?.run {
        attributes = attributes.also {
            it.width = (getWidth() * width).toInt()
            it.gravity = Gravity.CENTER
        }
    }
}

fun AlertDialog.fixShow(width: Float = 0.95f) = apply {
    this.show()
    this.window?.run {
        attributes = attributes.also {
            it.width = (getWidth() * width).toInt()
            it.gravity = Gravity.CENTER
        }
    }
}

fun String.alert(activity: AppCompatActivity){
//    MessageDialog(activity).setMessage(this).fixShow()
    LoadDialog(activity).setMessage(this).fixShow()
}
