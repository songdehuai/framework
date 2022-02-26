package com.ppz.framwork.widget

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ppz.framwork.ext.*
import com.ppz.framework.R


class TitleView : ConstraintLayout {

    private val defaultHeight by lazy { 48f.dp2px(context) }
    private val leftView by lazy { findViewById<ImageView>(R.id.iv_left) }
    private val rightText by lazy { findViewById<TextView>(R.id.tv_right) }
    private val rightImg by lazy { findViewById<ImageView>(R.id.iv_right) }
    private val titleTextView by lazy { findViewById<TextView>(R.id.tv_center) }
    private val clTitleRoot by lazy { findViewById<ConstraintLayout>(R.id.cl_title_root) }
    private val viewLine by lazy { findViewById<View>(R.id.view_line) }

    private var leftClick: (() -> Unit)? = null
    private var rightClick: (() -> Unit)? = null
    private var rightLongClick: (() -> Unit)? = null

    private var titleText = ""
    private var titleMode = 0
    private var rightImgRes = 0
    private var rigBackground: Int? = null
    private var viewLineHeight: Int = 1
    private var rightTextData: String? = null
    private var mode: TitleMode = TitleMode.TEXT


    constructor(context: Context?) : super(context!!) {
        initViews()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        initViews()
        initAttr(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context!!, attrs, defStyleAttr) {
        initViews()
        initAttr(attrs)
    }


    private fun initViews() {
        View.inflate(context, R.layout.view_title, this)
        loadViews()
    }

    private fun loadViews() {
        leftView.setOnClickListener {
            (leftClick != null).ifTrue {
                leftClick?.invoke()
            }.ifFalse {
                finishActivity()
            }
        }
        setRightImg()

        if (!rightTextData.isNullOrEmpty()) {
            rightText.text = rightTextData
        }
        when (mode) {
            TitleMode.NONE -> {

            }
            TitleMode.TEXT -> {
                rigBackground?.let {
                    rightText.setBackgroundResource(it)
                }
                rightText.setOnClickListener {
                    if (mode == TitleMode.TEXT) {
                        rightClick?.invoke()
                    }
                }

                rightText.setOnLongClickListener {
                    if (mode == TitleMode.TEXT) {
                        rightLongClick?.invoke()
                    }
                    false
                }
            }
            TitleMode.IMG -> {
                rigBackground?.let {
                    rightImg.setBackgroundResource(it)
                }
                rightImg.setOnClickListener {
                    if (mode == TitleMode.IMG) {
                        rightClick?.invoke()
                    }
                }
                rightImg.setOnLongClickListener {
                    if (mode == TitleMode.IMG) {
                        rightLongClick?.invoke()
                    }
                    false
                }
            }
        }

    }

    private fun finishActivity() {
        if (context is Activity) {
            (context as Activity).finish()
        }
    }

    @SuppressLint("Recycle")
    private fun initAttr(attrs: AttributeSet?) {
        val obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.TitleView)
        titleText = obtainStyledAttributes.getString(R.styleable.TitleView_title).toString()
        titleMode = obtainStyledAttributes.getInt(R.styleable.TitleView_titleMode, 0)
        rightImgRes = obtainStyledAttributes.getResourceId(R.styleable.TitleView_rightImg, 0)
        rightTextData = obtainStyledAttributes.getString(R.styleable.TitleView_rightText)
        rigBackground = obtainStyledAttributes.getResourceId(R.styleable.TitleView_rigBackground, 0)
        setMode(titleMode)
        setTitle(titleText)
        obtainStyledAttributes.recycle()

    }

    fun setRightImg() {
        rightImg.setImageResource(rightImgRes)
    }

    fun setTitleColor(color: Int) = apply {
        clTitleRoot.setBackgroundResource(color)
    }

    fun setTitle(title: String) = apply {
        titleTextView.setText(title)
    }

    fun setRightTitle(title: String) = apply {
        rightText.setText(title)
    }

    fun leftClick(action: (() -> Unit)) = apply {
        leftClick = action
    }

    fun rightClick(action: (() -> Unit)) = apply {
        rightClick = action
    }

    fun setTitleBarColor(color: Int) {
        setBackgroundColor(color)
    }

    fun setRightImgLongClick(action: (() -> Unit)) = apply {
        this.rightLongClick = action
    }


    private fun setMode(titleMode: Int) {
        when (titleMode) {
            0 -> {
                setMode(TitleMode.TEXT)
            }
            1 -> {
                setMode(TitleMode.IMG)
            }
            2 -> {
                setMode(TitleMode.NONE)
            }
        }
        loadViews()
    }

    fun setMode(mode: TitleMode) = apply {
        this.mode = mode
        when (mode) {
            TitleMode.IMG -> {
                rightImg.VISIBLE()
                rightText.INVISIBLE()
                titleMode = 1
            }
            TitleMode.TEXT -> {
                rightImg.INVISIBLE()
                rightText.VISIBLE()
                titleMode = 0
            }
            TitleMode.NONE -> {
                leftView.INVISIBLE()
                rightImg.INVISIBLE()
                rightText.INVISIBLE()
                titleMode = 2
            }
        }
        loadViews()
    }


    enum class TitleMode {
        IMG, TEXT, NONE
    }
}