package com.highstreet.wallet.gaojie

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import com.highstreet.lib.utils.T

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */

fun String.isName(): Boolean {
    return matches("[a-zA-Z0-9]{1,12}".toRegex())
}

fun String.isPassword(): Boolean {
    return matches("^(?![0-9]+\$)(?![a-zA-Z]+\$)[0-9A-Za-z]{8,16}\$".toRegex())
}

fun String.isAddress(): Boolean {
    return matches("^dip[a-zA-Z0-9]{39,100}".toRegex())
}

fun String.isAmount(): Boolean {
    return try {
        matches("[0-9.]{1,40}".toRegex())
    } catch (e: NumberFormatException) {
        false
    }
}

fun String.copy(context: Context) {
    if ("" == this) {
        return
    }
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    clipboardManager.primaryClip = ClipData.newPlainText(null, this)
    T.short(context, "已复制到剪粘板")
}

fun main() {

}