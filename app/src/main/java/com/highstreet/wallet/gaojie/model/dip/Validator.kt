package com.highstreet.wallet.gaojie.model.dip

import android.text.TextUtils
import com.highstreet.lib.adapter.BaseItem
import java.io.Serializable
import java.text.DecimalFormat

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

data class Validator(
        val commission: Commission?,
        val consensus_pubkey: String?,
        val delegator_shares: String?,
        val description: Description?,
        val jailed: Boolean?,
        val min_self_delegation: String?,
        val operator_address: String?,
        val self_delegation: String?,
        val status: Int?,
        val tokens: String?,
        val unbonding_height: String?,
        val unbonding_time: String?
) : BaseItem, Serializable {
    override fun uniqueKey(): String {
        return operator_address + consensus_pubkey
    }

    private var firstLetterName: String? = null

    fun getFirstLetterName(): String {

        if (null == firstLetterName) {
            val name = description?.moniker
            if (name == null || name.isEmpty()) {
                firstLetterName = ""
            } else {
                val l = name[0]

                firstLetterName = if (l in 'a'..'z' || l in 'A'..'Z') {
                    l.toString().toUpperCase()
                } else {
                    l.toString()
                }
            }
        }

        return firstLetterName!!
    }

    fun getRate(): String {

        val rate = commission?.commission_rates?.rate
        if (rate == null || rate.isEmpty()) {
            return ""
        }

        return "${DecimalFormat("0.00").format(rate.toDouble() * 100)}%"
    }

    fun getProfile(): String {
        val sb = StringBuilder()
        description?.apply {
            if (!TextUtils.isEmpty(identity)) {
                sb.append(identity).append("\n")
            }
            if (!TextUtils.isEmpty(website)) {
                sb.append(website).append("\n")
            }
            if (!TextUtils.isEmpty(details)) {
                sb.append(details)
            }
        }
        return sb.toString()
    }

}

data class Commission(
        val commission_rates: CommissionRates?,
        val update_time: String?
) : Serializable

data class Description(
        val details: String?,
        val identity: String?,
        val moniker: String?,
        val website: String?
) : Serializable

data class CommissionRates(
        val max_change_rate: String?,
        val max_rate: String?,
        val rate: String?
) : Serializable