package com.highstreet.wallet.gaojie.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.view.MotionEvent
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.wallet.R
import com.highstreet.wallet.gaojie.constant.ExtraKey
import com.king.zxing.CaptureHelper
import com.king.zxing.OnCaptureCallback
import kotlinx.android.synthetic.main.g_activity_scan.*


/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */

class ScanActivity : BaseActivity(), OnCaptureCallback {

    private var captureHelper: CaptureHelper? = null

    override fun showToolbar() = false

    override fun getLayoutId() = R.layout.g_activity_scan

    override fun initView() {

        getToolbar()?.apply {
            iconTintColor = Color.WHITE
            title = "扫码"
        }
//        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN)
//        val lp = window.attributes
//        lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
//        window.attributes = lp
    }

    override fun initData() {
        captureHelper = CaptureHelper(this, surfaceView, viewfinderView, null)
        captureHelper?.apply {
            setOnCaptureCallback(this@ScanActivity)
            onCreate()
            vibrate(false)
            fullScreenScan(true)
            supportVerticalCode(false)
            supportLuminanceInvert(false)
            continuousScan(false)
        }
    }


    override fun onResume() {
        super.onResume()
        captureHelper?.onResume()
    }

    override fun onPause() {
        super.onPause()
        captureHelper?.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        captureHelper?.onDestroy()
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        captureHelper?.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    override fun onResultCallback(result: String): Boolean {
        val intent = Intent()
        intent.putExtra(ExtraKey.STRING, result)
        setResult(RESULT_OK, intent)
        finish()
        return true
    }

    companion object {
        const val REQUEST_CODE_SCAN = 102

        fun start(context: Activity) {
            context.startActivityForResult(Intent(context, ScanActivity::class.java), REQUEST_CODE_SCAN)
        }
    }
}