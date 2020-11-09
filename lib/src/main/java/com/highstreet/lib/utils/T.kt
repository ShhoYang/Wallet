package com.highstreet.lib.utils

import android.content.Context
import android.support.annotation.StringRes
import android.text.TextUtils
import android.widget.Toast

/**
 * @author Yang Shihao
 * @Date 2020/7/20
 */
object T {
    fun short(context: Context?, msg: String?) {
        if (null == context || TextUtils.isEmpty(msg)) {
            return
        }
        val toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        toast.show()
    }

    fun short(context: Context?, @StringRes resId: Int) {
        if (null == context) {
            return
        }
        val toast = Toast.makeText(context, resId, Toast.LENGTH_SHORT)
        toast.show()
    }
}