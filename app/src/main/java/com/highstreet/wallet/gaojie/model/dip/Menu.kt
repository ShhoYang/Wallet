package com.highstreet.wallet.gaojie.model.dip

import android.app.Activity

/**
 * @author Yang Shihao
 * @Date 2020/10/20
 */
data class Menu(
        val text: String,
        val icon: Int = 0,
        val cls: Class<out Activity>? = null,
        val type: Int = TYPE_NORMAL
) {

    companion object {
        const val TYPE_NORMAL = 0
        const val TYPE_WIDE_LINE = 1
        const val TYPE_NARROW_LINE = 2

        fun wide() = Menu("", 0, null, TYPE_WIDE_LINE)
        fun narrow() = Menu("", 0, null, TYPE_NARROW_LINE)
    }
}