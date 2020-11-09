package com.highstreet.wallet.gaojie.model.dip

/**
 * @author Yang Shihao
 * @Date 2020/10/19
 */
data class Transaction(
        val count: String?,
        val limit: String?,
        val page_number: String?,
        val page_total: String?,
        val total_count: String?,
        val txs: ArrayList<Tx>?
)