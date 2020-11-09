package com.highstreet.wallet.gaojie.model.dip

import com.highstreet.lib.adapter.BaseItem
import java.io.Serializable

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */

data class DelegationInfo(
        val balance: Coin?,
        val delegator_address: String,
        val entries: ArrayList<Entry>?,
        val shares: String?,
        val validator_address: String
) : BaseItem, Serializable {

    var completionTime = ""

    override fun uniqueKey(): String {
        return delegator_address + validator_address + shares
    }
}

data class Entry(
        val balance: String,
        val completion_time: String,
        val creation_height: String,
        val initial_balance: String
) : Serializable
