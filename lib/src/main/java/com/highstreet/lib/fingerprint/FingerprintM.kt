package com.highstreet.lib.fingerprint

import android.app.Activity
import android.os.Handler
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat
import android.support.v4.os.CancellationSignal
import com.highstreet.lib.fingerprint.listener.FingerprintDialogListener
import com.highstreet.lib.fingerprint.listener.FingerprintCallback
import com.socks.library.KLog

/**
 * @author Yang Shihao
 * @Date 2020/10/20
 */
class FingerprintM : FingerprintManagerCompat.AuthenticationCallback(), IFingerprint, FingerprintDialogListener {

    private val handler = Handler()

    private var useFingerprint = false

    private var fingerprintDialog: FingerprintDialog? = null

    //指向调用者的指纹回调
    private var fingerprintCallback: FingerprintCallback? = null

    //指纹加密
    private lateinit var cryptoObject: FingerprintManagerCompat.CryptoObject

    //Android 6.0 指纹管理
    private lateinit var fingerprintManagerCompat: FingerprintManagerCompat

    private var cancellationSignal: CancellationSignal? = null

    override fun init(context: Activity?, useFingerprint: Boolean, fingerprintCallback: FingerprintCallback?, dialogParams: DialogParams) {
        if (null == context) {
            return
        }
        this.fingerprintCallback = fingerprintCallback
        this.useFingerprint = useFingerprint
        fingerprintManagerCompat = FingerprintManagerCompat.from(context)

        cryptoObject = FingerprintManagerCompat.CryptoObject(CipherHelper().createCipher())

        fingerprintDialog = FingerprintDialog(context)
        fingerprintDialog?.setDialogParams(dialogParams)?.setListener(this)
    }

    override fun authenticate() {
        fingerprintDialog?.show()
        if (useFingerprint) {
            cancellationSignal = CancellationSignal()
            cancellationSignal?.setOnCancelListener {
            }
            fingerprintManagerCompat.authenticate(cryptoObject, 0, cancellationSignal, this, null)
        }
    }

    override fun onStop() {
        fingerprintDialog?.dismiss()
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        fingerprintDialog = null
    }

    /**
     * dialog的监听
     *
     * 取消
     */
    override fun cancelFingerprint() {
        cancellationSignal()
        fingerprintCallback?.onFingerprintCancel()
    }

    /**
     * dialog的监听
     *
     * 密码验证
     */
    override fun usePassword(password: String): Boolean? {
        return fingerprintCallback?.usePassword(password)
    }

    private fun cancellationSignal() {
        cancellationSignal?.apply {
            if (!isCanceled) {
                cancel()
            }
        }
    }

    /**
     * FingerprintManagerCompat.AuthenticationCallback的回调
     */
    override fun onAuthenticationError(errMsgId: Int, errString: CharSequence) {
        super.onAuthenticationError(errMsgId, errString)
        KLog.d("onAuthenticationError", "msgId = ${errMsgId},msg = ${errString}")
        failed(errString?.toString())
    }

    override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence) {
        super.onAuthenticationHelp(helpMsgId, helpString)
        failed(helpString?.toString())
    }

    override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult) {
        super.onAuthenticationSucceeded(result)
        succeed()
    }

    override fun onAuthenticationFailed() {
        super.onAuthenticationFailed()
        failed("验证失败")
    }

    private fun failed(msg: String) {
        fingerprintDialog?.setTip(msg, false)
    }

    private fun succeed() {
        fingerprintDialog?.setTip("验证成功", true)
        cancellationSignal()

        handler.postDelayed({
            fingerprintDialog?.dismiss()
            fingerprintCallback?.onFingerprintAuthenticateSucceed()
        }, 500)
    }
}