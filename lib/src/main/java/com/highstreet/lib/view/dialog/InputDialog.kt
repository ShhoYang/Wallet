package com.highstreet.lib.view.dialog

import android.app.Activity
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.TextView
import com.highstreet.lib.R
import com.highstreet.lib.extensions.string
import com.highstreet.lib.utils.DisplayUtils
import com.highstreet.lib.utils.DrawableUtils

/**
 * @author Yang Shihao
 * @Date 2020/10/21
 */

class InputDialog(activity: Activity) : CommonDialog(activity = activity), View.OnClickListener{

    private var tvTitle: TextView? = null
    private var etContent: EditText? = null
    private var tvCancel: TextView? = null
    private var tvConfirm: TextView? = null

    private var inputDialogListener: InputDialogListener? = null

    override fun setParams(window: Window) {
        initView()
        val attributes = window.attributes
        val w = DisplayUtils.getScreenWidth(activity)
        attributes.width = w / 10 * 8
        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
        attributes.gravity = Gravity.CENTER
        window.attributes = attributes
        window.setBackgroundDrawable(DrawableUtils.generateRoundRectDrawable(12.0F, Color.WHITE))
        setCancelable(false)
    }

    fun initView() {
        val view = View.inflate(activity, R.layout.dialog_input, null)
        setContentView(view)
        tvTitle = view.findViewById(R.id.tvTitle)
        etContent = view.findViewById(R.id.etContent)
        tvCancel = view.findViewById(R.id.tvCancel)
        tvConfirm = view.findViewById(R.id.tvConfirm)

        val drawable = DrawableUtils.generateRoundRectBorderDrawable(12.0F, DisplayUtils.dp2px(activity, 1), ContextCompat.getColor(activity, R.color.colorPrimary9))
        etContent?.background = drawable
        tvCancel?.setOnClickListener(this)
        tvConfirm?.setOnClickListener(this)
    }

    fun setTitle(title: String): InputDialog {
        tvTitle?.text = title
        return this
    }

    fun setHint(hint: String): InputDialog {
        etContent?.hint = hint
        return this
    }

    fun setText(text: String): InputDialog {
        etContent?.setText(text)
        return this
    }

    fun setListener(inputDialogListener: InputDialogListener?): InputDialog {
        this.inputDialogListener = inputDialogListener
        return this
    }

    override fun onClick(v: View?) {
        if (v == tvConfirm) {
            inputDialogListener?.confirm(etContent?.string() ?: "")
        }
        dismiss()
    }
}

interface InputDialogListener {
    fun confirm(content: String)
}