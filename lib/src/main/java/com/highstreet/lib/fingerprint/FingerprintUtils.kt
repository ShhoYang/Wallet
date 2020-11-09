package com.highstreet.lib.fingerprint

import android.content.Context
import android.os.Build
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat

/**
 * @author Yang Shihao
 * @Date 2020/10/20
 */
object FingerprintUtils {

    /**
     * 指纹是否可用
     */
    fun isAvailable(context: Context?): Boolean {

        if (null == context) {
            return false
        }

        return isHardwareDetected(context) && hasEnrolledFingerprints(context)
    }

    /**
     * 设备是否支持指纹
     */
    fun isHardwareDetected(context: Context?): Boolean {

        if (null == context) {
            return false
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false
        }
        return FingerprintManagerCompat.from(context).isHardwareDetected
    }

    /**
     * 是否录入指纹
     */
    fun hasEnrolledFingerprints(context: Context?): Boolean {

        if (null == context) {
            return false
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return false
        }
        return FingerprintManagerCompat.from(context).hasEnrolledFingerprints()
    }
}