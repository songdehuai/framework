package com.ppz.framwork.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.ppz.framework.R
import com.ppz.framwork.ext.GONE
import com.ppz.framwork.ext.VISIBLE
import com.ppz.framwork.ext.autoInvisible

class EmptyView : LinearLayout {

    private val tvEmpty by lazy { findViewById<TextView>(R.id.tv_empty) }
    private var onClickAction: (() -> Unit)? = null

    constructor(context: Context) : super(context) {
        initViews()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initViews()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initViews()
    }

    private fun initViews() {
        inflate(context, R.layout.view_empty, this)
        setOnClickListener {
            onClickAction?.invoke()
        }
    }

    fun show() {
        this.VISIBLE()
    }

    fun hide() {
        this.GONE()
    }

    fun autoShow(boolean: Boolean) {
        this.autoInvisible(boolean)
    }

    fun setText(text: CharSequence) {
        tvEmpty.text = text
    }


    fun click(onClickAction: (() -> Unit)) {
        this.onClickAction = onClickAction
    }

}