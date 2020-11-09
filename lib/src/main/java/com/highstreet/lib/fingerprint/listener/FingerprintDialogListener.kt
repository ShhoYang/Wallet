package com.highstreet.lib.fingerprint.listener

/**
 * @author Yang Shihao
 * @Date 2020/10/21
 */

interface FingerprintDialogListener {

    fun cancelFingerprint()

    fun usePassword(password: String): Boolean?
}