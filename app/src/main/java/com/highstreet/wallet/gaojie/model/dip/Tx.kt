package com.highstreet.wallet.gaojie.model.dip

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.highstreet.lib.adapter.BaseItem
import com.highstreet.wallet.gaojie.constant.Constant
import com.highstreet.wallet.model.StdTx
import com.highstreet.wallet.model.type.Coin
import java.lang.Exception
import java.math.BigDecimal
import java.text.DecimalFormat
import kotlin.collections.ArrayList

/**
 * @author Yang Shihao
 * @Date 2020/10/26
 */
data class Tx(
        val gas_used: String?,
        val logs: List<Log>?,
        val timestamp: String?,
        val txhash: String?,
        val tx: StdTx?
) : BaseItem {

    fun success(): Boolean {
        return if (logs == null || logs.isEmpty()) {
            false
        } else {
            true == logs[0].success
        }
    }

    /**
     * 费用
     */
    fun getFee(): String {
        if (gas_used == null || !TextUtils.isDigitsOnly(gas_used)) {
            return ""
        }

        val gas = tx?.value?.fee?.gas
        if (TextUtils.isEmpty(gas)) {
            return ""
        }

        val wantedFee = tx?.value?.fee?.amount
        val feeAmount = if (wantedFee == null || wantedFee.isEmpty()) {
            null
        } else {
            wantedFee[0]
        }

        if (feeAmount == null || TextUtils.isEmpty(feeAmount.amount)) {
            return ""
        }

        var r = BigDecimal(feeAmount.amount).divide(BigDecimal(gas)).multiply(BigDecimal(gas_used))
        if ("pdip" == feeAmount.denom) {
            r = r.divide(BigDecimal(Constant.DIP_RATE))
        }
        val df = DecimalFormat("#.######")
        return "${df.format(r.toDouble())}DIP"
    }

    /**
     * 金额
     */
    fun getAmount(): Coin? {
        val msgs = tx?.value?.msg
        val msg = if (msgs == null || msgs.isEmpty()) {
            null
        } else {
            msgs[0].value
        }

        var coins = if (msg?.amount == null) {
            null
        } else {
            try {
                Gson().fromJson<ArrayList<Coin>>(Gson().toJson(msg?.amount
                        ?: "[]"), object : TypeToken<List<Coin>>() {}.type)
            } catch (e: Exception) {
                val coin = Gson().fromJson<Coin>(Gson().toJson(msg?.amount
                        ?: "{}"), object : TypeToken<Coin>() {}.type)
                arrayListOf(coin)
            }
        }
        return if (coins == null || coins.isEmpty()) {
            null
        } else {
            coins[0]
        }
    }

    /**
     * 转入地址
     */
    fun getFromAddress(): String {
        val msgs = tx?.value?.msg
        val msg = if (msgs == null || msgs.isEmpty()) {
            null
        } else {
            msgs[0].value
        }

        return msg?.from_address ?: msg?.validator_address ?: ""
    }

    /**
     * 转出地址
     */
    fun getToAddress(): String {
        val msgs = tx?.value?.msg
        val msg = if (msgs == null || msgs.isEmpty()) {
            null
        } else {
            msgs[0].value
        }
        return msg?.to_address ?: ""
    }

    /**
     * 验证人地址
     */
    fun getValidatorAddress(): String {
        val msgs = tx?.value?.msg
        val msg = if (msgs == null || msgs.isEmpty()) {
            null
        } else {
            msgs[0].value
        }
        return msg?.validator_address ?: msg?.validator_dst_address ?: ""
    }

    override fun uniqueKey(): String {
        return "$txhash"
    }
}

data class Log(
        val success: Boolean?
)