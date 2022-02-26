package com.ppz.framwork.ext

import android.app.Activity
import android.view.View
import android.widget.SeekBar
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.ppz.framwork.App
import com.ppz.framwork.tools.MyDividerItemDecoration
import com.ppz.framwork.tools.ViewPager2Adapter


fun View.topPadding() {
    this.setPadding(0, App.statusHeight, 0, 0)
}

fun View.VISIBLE() {
    this.visibility = View.VISIBLE
}

fun View.GONE() {
    this.visibility = View.GONE
}

fun View.enabled() {
    this.isEnabled = true
}

fun View.disable() {
    this.isEnabled = false
}

fun View.autoInvisible(enable: Boolean) {
    if (enable) {
        this.VISIBLE()
    } else {
        this.GONE()
    }
}

fun View.autoEnable(enable: Boolean) {
    if (enable) {
        this.enabled()
    } else {
        this.disable()
    }
}

fun View.INVISIBLE() {
    this.visibility = View.INVISIBLE
}

fun View.finishActivity(){
    if(this.context is Activity){
        (this.context as Activity).finish()
    }

}
fun SwipeRefreshLayout.finish() {
    this.isRefreshing = false
}

fun SwipeRefreshLayout.start() {
    this.isRefreshing = true
}


fun ViewPager2.withTabLayout(tabLayout: TabLayout) {
    TabLayoutMediator(tabLayout, this, true) { tab, position ->
        if (this.adapter is ViewPager2Adapter<*>) {
            tab.text = (this.adapter as ViewPager2Adapter<*>).getTitles(position)
        }
    }.attach()
}


internal infix fun SeekBar.onProgressChanged(zoomUpdated: () -> Unit) {
    setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            zoomUpdated()
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {

        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {

        }
    })
}

fun RecyclerView.addLine(colorString: String = "#FAFAFA", lineWidth: Int = 1) {
    this.addItemDecoration(
        MyDividerItemDecoration()
            .setColorString(colorString)
            .setLineWidth(lineWidth)
    )
}

fun RecyclerView.addLine() {
    this.addItemDecoration(
        MyDividerItemDecoration()
            .setColorString("#0F000000")
            .setLineWidth(1)
    )
}

fun RecyclerView.gridLine() {
    this.addItemDecoration(
        MyDividerItemDecoration()
            .setColorString("#ffffff")
            .setDrawOuterBorder(true)
            .setLineWidth(18)
    )
}


