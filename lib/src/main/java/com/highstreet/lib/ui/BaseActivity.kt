package com.highstreet.lib.ui

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.highstreet.lib.R
import com.highstreet.lib.common.AppManager
import com.highstreet.lib.fingerprint.DialogParams
import com.highstreet.lib.fingerprint.FingerprintM
import com.highstreet.lib.fingerprint.IFingerprint
import com.highstreet.lib.fingerprint.listener.FingerprintCallback
import com.highstreet.lib.view.ToolbarLayout
import com.highstreet.lib.view.dialog.LoadingDialog
import kotlinx.android.synthetic.main.activity_base.*

/**
 * @author Yang Shihao
 * @date 2018/11/18
 */
abstract class BaseActivity : AppCompatActivity(), FingerprintCallback {

    companion object {
        private const val STATUS_BAR_DARK = 0
        private const val STATUS_BAR_LIGHT = 1
    }

    private var toolbar: ToolbarLayout? = null

    private var loadingDialog: LoadingDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppManager.instance().pushActivity(this)
        prepare(savedInstanceState)
        if (!showToolbar()) {
            setContentView(getLayoutId())
        } else {
            setContentView(R.layout.activity_base)
            View.inflate(this, getLayoutId(), activityRootView)
        }
        toolbar = findViewById(R.id.baseToolbar)
        toolbar?.apply {
            setBackClickListener {
                onBackPressed()
            }
        }
        initView()
        initData()
    }

    override fun onPause() {
        super.onPause()
        hideLoading()
    }

    override fun onDestroy() {
        super.onDestroy()
        toastCancel()
        loadingDialog = null
        AppManager.instance().popActivity(this)
    }

    /**
     * 改变状态栏样式
     */
    fun setStatusBarMode(mode: Int, transparentStatusBar: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var systemUiVisibility = window.decorView.systemUiVisibility

            if (STATUS_BAR_LIGHT == mode) {
                systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else if (STATUS_BAR_DARK == mode) {
                systemUiVisibility =
                        systemUiVisibility and (View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv())
            }

            if (transparentStatusBar) {
                systemUiVisibility = systemUiVisibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }

            window.decorView.systemUiVisibility = systemUiVisibility
        }
    }

    open fun showToolbar() = true

    @LayoutRes
    abstract fun getLayoutId(): Int

    open fun prepare(savedInstanceState: Bundle?) {

    }

    open fun initView() {
    }

    open fun initData() {
    }

    override fun setTitle(title: CharSequence?) {
        toolbar?.title = title
    }

    /**
     * 设置透明状态栏，耗时5ms
     * 只在主题设置，某些机型会是半透明的
     *
     * @param lightStatusBar true深色文字，false浅色文字
     */
    fun transparentStatusBar(lightStatusBar: Boolean = true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (lightStatusBar) {
                var systemUiVisibility = (window.decorView.systemUiVisibility
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                window.decorView.systemUiVisibility = systemUiVisibility
            } else {
                var systemUiVisibility = (window.decorView.systemUiVisibility
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv())
                window.decorView.systemUiVisibility = systemUiVisibility
            }
        }
    }

    fun getToolbar() = toolbar

    fun showLoading() {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog(this)
        }
        loadingDialog?.show()
    }

    fun hideLoading() {
        if (loadingDialog?.isShowing == true) {
            loadingDialog?.dismiss()

        }
    }

    private var toast: Toast? = null

    fun toast(msg: String?) {
        if (TextUtils.isEmpty(msg)) {
            return
        }
        try {
            if (toast == null) {
                toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT)
            } else {
                toast!!.setText(msg)
                toast!!.duration = Toast.LENGTH_SHORT
            }
            toast!!.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun toast(@StringRes resId: Int) {
        toast(getString(resId))
    }

    private fun toastCancel() {
        try {
            toast?.cancel()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun go(cls: Class<out Activity>, isFinish: Boolean = false) {
        startActivity(Intent(this, cls))
        if (isFinish) {
            finish()
        }
    }

    /**
     * 指纹
     */
    private var fingerprint: IFingerprint? = null

    protected fun getFingerprint(useFingerprint: Boolean, showUserPassword: Boolean): IFingerprint? {
        if (null == fingerprint) {
            fingerprint = FingerprintM()
            fingerprint!!.init(this,
                    this,
                    DialogParams(useFingerprint = useFingerprint, showUserPassword = showUserPassword)
            )
        }

        return fingerprint
    }

    override fun onFingerprintAuthenticateSucceed() {

    }

    override fun onFingerprintCancel() {

    }

    override fun usePassword(password: String): Boolean {
        return false
    }

}