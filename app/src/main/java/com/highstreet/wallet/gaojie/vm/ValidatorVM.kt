package com.highstreet.wallet.gaojie.vm

import android.os.Handler
import android.os.Looper
import com.highstreet.lib.viewmodel.BaseListViewModel
import com.highstreet.wallet.gaojie.constant.Constant
import com.highstreet.wallet.gaojie.http.ApiService
import com.highstreet.wallet.gaojie.http.subscribeBy
import com.highstreet.wallet.gaojie.model.dip.Validator
import kotlin.collections.ArrayList

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */
class ValidatorVM : BaseListViewModel<Validator>() {

    var filterType = Constant.VALIDATOR_ALL

    private var sortType = Constant.SORT_SHARES_DESC

    private val handler = Handler()

    private val list = ArrayList<Validator>()

    fun filter(filterType: Int, sortType: Int) {
        if (this.filterType != filterType || this.sortType != sortType) {
            this.filterType = filterType
            this.sortType = sortType
            invalidate()
        }
    }

    override fun pageSize() = Int.MAX_VALUE

    override fun loadData(page: Int, onResponse: (ArrayList<Validator>?) -> Unit) {

        if (list.isEmpty()) {
            ApiService.getDipApi().validators(page, pageSize()).subscribeBy({
                list.clear()
                val data = it.result
                if (data != null && data.isNotEmpty()) {
                    list.addAll(data)
                }
                filterData(onResponse)
            }, {
                onResponse(null)
            }).add()
        } else {
            filterData(onResponse)
        }
    }

    private fun filterData(onResponse: (ArrayList<Validator>?) -> Unit) {
        val ret = ArrayList<Validator>()
        when (filterType) {
            Constant.VALIDATOR_BONDED -> {
                val temp = list.filter {
                    Constant.VALIDATOR_BONDED == it.status
                }
                if (temp != null && temp.isNotEmpty()) {
                    ret.addAll(temp)
                }
            }
            Constant.VALIDATOR_JAILED -> {
                val temp = list.filter {
                    it.jailed == true
                }
                if (temp != null && temp.isNotEmpty()) {
                    ret.addAll(temp)
                }
            }
            else -> {
                if (list.isNotEmpty()) {
                    ret.addAll(list)
                }
            }
        }

        ret.sortBy { v ->
            when (sortType) {
                Constant.SORT_RATE_DESC -> (v.commission?.commission_rates?.rate?.toDouble()
                        ?: 0.0) / (-1.0)
                Constant.SORT_SHARES_DESC -> (v.delegator_shares?.toDouble() ?: 0.0) / (-1.0)
                Constant.SORT_SHARES_ASC -> v.delegator_shares?.toDouble() ?: 0.0
                else -> v.commission?.commission_rates?.rate?.toDouble() ?: 0.0
            }
        }

        if (Looper.myLooper() == Looper.getMainLooper()) {
            onResponse(ret)
        } else {
            handler.post {
                onResponse(ret)
            }
        }
    }
}