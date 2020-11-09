package com.highstreet.lib.view.dialog

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.view.Window
import com.highstreet.lib.R
import com.highstreet.lib.utils.DisplayUtils
import com.highstreet.lib.utils.DrawableUtils

/**
 * @author Yang Shihao
 * @Date 2019-06-29
 */
class LoadingDialog(activity: Activity) : CommonDialog(activity = activity, themeResId = R.style.Dialog2) {

    override fun setParams(window: Window) {
        initView()
        val attributes = window.attributes
        val w = DisplayUtils.dp2px(activity, 128)
        attributes.width = w
        attributes.height = w
        attributes.gravity = Gravity.CENTER
        window.attributes = attributes
        window.setBackgroundDrawable(DrawableUtils.generateRoundRectDrawable(12.0F,0x60000000))
        setCancelable(false)
    }

    fun initView() {
        val view = View.inflate(activity, R.layout.dialog_loading, null)
        setContentView(view)
    }
}