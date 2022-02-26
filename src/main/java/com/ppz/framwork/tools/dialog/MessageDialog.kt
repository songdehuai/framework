package com.ppz.framwork.tools.dialog


import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.Utils
import com.ppz.framwork.ext.dp2px
import com.ppz.framework.R
import com.ppz.framwork.global.TipType


class MessageDialog(activity: AppCompatActivity) : AlertDialog(activity) {

    private val padding by lazy { 12f.dp2px(activity) }
    private val viewSize by lazy { 128f.dp2px(activity) }
    private val dialogSize by lazy { padding + viewSize }


    private val typeIcon by lazy {
        ImageView(context).also {
            it.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).also {
                it.topMargin = padding
            }
            it.setImageDrawable(ContextCompat.getDrawable(Utils.getApp(), R.drawable.ic_warning))
        }
    }

    private val dialogText by lazy {
        TextView(context).also {
            it.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT).also {
                it.topMargin = padding
            }
            it.text = "加载中"
        }
    }
    private var message = "加载中"

    private val dialogView by lazy {
        LinearLayout(context).also {
            it.layoutParams = WindowManager.LayoutParams(viewSize, viewSize)
            it.orientation = LinearLayout.VERTICAL
            it.gravity = Gravity.CENTER
            it.setPadding(padding, padding, padding, padding)
            it.addView(typeIcon)
            // TODO 不显示文字，请教德怀
            it.addView(dialogText)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(dialogView)
        init()
    }

    private fun init() {
        setCancelable(true)
    }

    fun setMessage(message: String) = apply {
        this.message = message
    }

    fun setType(type: TipType) = apply {
        when (type) {
            TipType.SUCCESS -> {
                typeIcon.setImageDrawable(ContextCompat.getDrawable(Utils.getApp(), R.drawable.ic_success))
            }
            TipType.ERROR -> {
                typeIcon.setImageDrawable(ContextCompat.getDrawable(Utils.getApp(), R.drawable.ic_error))
            }
            TipType.WARNING -> {
                typeIcon.setImageDrawable(ContextCompat.getDrawable(Utils.getApp(), R.drawable.ic_warning))
            }
            TipType.WTF -> {
                typeIcon.setImageDrawable(ContextCompat.getDrawable(Utils.getApp(), R.drawable.ic_wtf))
            }
        }
    }

    override fun show() {
        super.show()
        dialogText.text = message
        val lp: WindowManager.LayoutParams? = window?.attributes
        lp?.gravity = Gravity.CENTER
//        lp?.dimAmount = 0f
        lp?.width = dialogSize
        lp?.height = dialogSize
        window?.attributes = lp

    }

}