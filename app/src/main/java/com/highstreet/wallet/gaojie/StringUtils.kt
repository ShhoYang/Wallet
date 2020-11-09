package com.highstreet.wallet.gaojie

import android.text.TextUtils
import com.highstreet.wallet.gaojie.model.dip.Coin
import org.w3c.dom.Text
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

object StringUtils {

    private const val SECOND = 1000
    private const val MINUTE = SECOND * 60
    private const val HOUR = MINUTE * 60
    private const val DAY = HOUR * 24

    private const val PDIP = "pdip"
    private const val DIP = "DIP"
    private const val DIP_RATE = 1_000_000_000_000

    fun formatDecimal(s: String?): String {
        if (s == null || s.isEmpty()) {
            return ""
        }

        if (s.contains(".")) {
            return s.substring(0, s.indexOf("."))
        }
        return s
    }

    fun pdip2DIP(coin: Coin?): String {
        if (coin == null) {
            return "0DIP"
        }

        return pdip2DIP("${coin.amount}${coin.denom}")
    }

    fun pdip2DIP(coin: com.highstreet.wallet.model.type.Coin?): String {
        if (coin == null) {
            return "0DIP"
        }

        return pdip2DIP("${coin.amount}${coin.denom}")
    }

    fun pdip2DIP(amount: String?): String {
        if (amount == null || amount.isEmpty()) {
            return "0DIP"
        }

        if (amount.endsWith("DIP")) {
            return amount
        }

        val temp = if (amount.endsWith(PDIP)) {
            amount.replace(PDIP, "")
        } else {
            amount
        }

        if (temp == "" || temp == "0") {
            return "0DIP"
        }

        val df = DecimalFormat("#.######")
        if (TextUtils.isDigitsOnly(temp)) {
            val l = temp.toLong().toDouble() / DIP_RATE

            return "${df.format(l)}DIP"
        } else if (temp.contains(".")) {
            val l = temp.toDouble() / DIP_RATE
            return "${df.format(l)}DIP"
        }
        return amount
    }

    fun utc2String(s: String?): String {
        val date = utc2Date(s) ?: return ""
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            sdf.format(date)
        } catch (e: Exception) {
            ""
        }
    }

    fun timeGap(s: String?): String {
        val temp = utc2Date(s)?.time ?: return ""
        val cur = System.currentTimeMillis()
        val gap = temp - cur
        if (gap <= SECOND) {
            return "0秒"
        }

        val day = gap / DAY
        val hour = (gap % DAY) / HOUR
        val minute = (gap % HOUR) / MINUTE
        val second = (gap % MINUTE) / SECOND

        return when {
            gap > day -> {
                "${day}天${hour}小时${minute}分${second}秒"
            }
            gap > hour -> {
                "${hour}小时${minute}分${second}秒"
            }
            gap > minute -> {
                "${minute}分${second}秒"
            }
            else -> {
                "${second}秒"
            }
        }
    }

    private fun utc2Date(s: String?): Date? {
        if (s == null || s.isEmpty()) {
            return null
        }
        var text = if (s.indexOf(".") > -1) {
            s.substring(0, s.indexOf(".")) + "Z"
        } else {
            s
        }

        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            sdf.timeZone = TimeZone.getTimeZone("UTC")
            sdf.parse(text)
        } catch (e: Exception) {
            null
        }
    }
}