package com.highstreet.lib.fingerprint

import android.app.Activity
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.highstreet.lib.R
import com.highstreet.lib.extensions.gone
import com.highstreet.lib.extensions.string
import com.highstreet.lib.extensions.visible
import com.highstreet.lib.fingerprint.listener.FingerprintDialogListener
import com.highstreet.lib.utils.DisplayUtils
import com.highstreet.lib.utils.DrawableUtils
import com.highstreet.lib.view.dialog.CommonDialog

/**
 * @author Yang Shihao
 * @Date 2020/10/20
 */
class FingerprintDialog(activity: Activity) : CommonDialog(activity = activity), View.OnClickListener {

    private var ivFingerprint: ImageView? = null
    private var tvMsg: TextView? = null
    private var lineV: View? = null
    private var tvCancel: TextView? = null
    private var tvUsePassword: TextView? = null
    private var llFingerprint: LinearLayout? = null

    private var etPassword: EditText? = null
    private var tvCancel2: TextView? = null
    private var tvConfirm: TextView? = null
    private var llPassword: LinearLayout? = null

    private val colorFailed = 0xFFDC143C.toInt()
    private val colorSucceed = 0xFF3B64DB.toInt()
    private val colorTip = 0xFF353535.toInt()

    private var dialogParams: DialogParams = DialogParams(useFingerprint = true, showUserPassword = true)

    private var fingerprintDialogListener: FingerprintDialogListener? = null

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
        val view = View.inflate(activity, R.layout.dialog_fingerprint, null)
        setContentView(view)
        ivFingerprint = view.findViewById(R.id.ivFingerprint)
        tvMsg = view.findViewById(R.id.tvMsg)
        lineV = view.findViewById(R.id.lineV)
        tvCancel = view.findViewById(R.id.tvCancel)
        tvUsePassword = view.findViewById(R.id.tvUsePassword)
        llFingerprint = view.findViewById(R.id.llFingerprint)
        etPassword = view.findViewById(R.id.etPassword)
        tvCancel2 = view.findViewById(R.id.tvCancel2)
        tvConfirm = view.findViewById(R.id.tvConfirm)
        llPassword = view.findViewById(R.id.llPassword)

        val drawable = DrawableUtils.generateRoundRectBorderDrawable(12.0F, DisplayUtils.dp2px(activity, 1), ContextCompat.getColor(activity, R.color.colorPrimary9))
        etPassword?.background = drawable

        tvCancel?.setOnClickListener(this)
        tvCancel2?.setOnClickListener(this)
        tvUsePassword?.setOnClickListener(this)
        tvConfirm?.setOnClickListener(this)

        setOnCancelListener {
            fingerprintDialogListener?.cancelFingerprint()
        }
    }

    override fun show() {
        reset()
        super.show()
    }

    private fun reset() {
        tvMsg?.text = "请验证您的指纹"
        tvMsg?.setTextColor(colorTip)
        ivFingerprint?.setImageResource(R.mipmap.fingerprint)
        etPassword?.setText("")
        etPassword?.error = null
        if (dialogParams.useFingerprint) {
            llFingerprint?.visible()
            llPassword?.gone()
        } else {
            llFingerprint?.gone()
            llPassword?.visible()
        }
    }

    fun setTip(msg: String, isSucceed: Boolean) {
        tvMsg?.text = msg
        if (isSucceed) {
            tvMsg?.setTextColor(colorSucceed)
            ivFingerprint?.setImageResource(R.mipmap.fingerprint_1)
        } else {
            tvMsg?.setTextColor(colorFailed)
            ivFingerprint?.setImageResource(R.mipmap.fingerprint)
        }
    }

    fun setDialogParams(dialogParams: DialogParams): FingerprintDialog {
        this.dialogParams = dialogParams
        if (dialogParams.showUserPassword) {
            lineV?.visible()
            tvUsePassword?.visible()
        } else {
            lineV?.gone()
            tvUsePassword?.gone()
        }
        return this
    }

    fun setListener(fingerprintDialogListener: FingerprintDialogListener?): FingerprintDialog {
        this.fingerprintDialogListener = fingerprintDialogListener
        return this
    }

    override fun onClick(v: View?) {
        when (v) {
            tvCancel, tvCancel2 -> {
                fingerprintDialogListener?.cancelFingerprint()
                dismiss()
            }

            tvUsePassword -> {
                fingerprintDialogListener?.cancelFingerprint()
                llFingerprint?.gone()
                llPassword?.visible()
            }

            tvConfirm -> {
                if (true == fingerprintDialogListener?.usePassword(etPassword?.string() ?: "")) {
                    dismiss()
                } else {
                    etPassword?.error = "密码错误"
                }
            }
        }
    }
}