package com.highstreet.lib.fingerprint

import android.app.Activity
import com.highstreet.lib.fingerprint.listener.FingerprintCallback

/**
 * @author Yang Shihao
 * @Date 2020/10/20
 */
interface IFingerprint {

    fun init(context: Activity?, fingerprintCallback: FingerprintCallback?, dialogParams: DialogParams)

    fun authenticate()

    fun onStop()

    fun onDestroy()
}