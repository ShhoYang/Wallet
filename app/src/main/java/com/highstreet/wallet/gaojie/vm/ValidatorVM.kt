package com.highstreet.wallet.gaojie.vm

import com.highstreet.lib.viewmodel.BaseListViewModel
import com.highstreet.wallet.gaojie.fragment.ValidatorFragment
import com.highstreet.wallet.gaojie.http.ApiService
import com.highstreet.wallet.gaojie.http.subscribeBy
import com.highstreet.wallet.gaojie.model.dip.Validator

/**
 * @author Yang Shihao
 * @Date 2020/10/24
 */
class ValidatorVM : BaseListViewModel<Validator>() {

    var type = ValidatorFragment.TYPE_ALL

    override fun loadData(page: Int, onResponse: (ArrayList<Validator>?) -> Unit) {
        ApiService.getDipApi().validators(page, pageSize()).subscribeBy({
            onResponse(handle(it.result))
        }, {
            onResponse(null)
        }).add()
    }

    private fun handle(list: ArrayList<Validator>?): ArrayList<Validator> {
        val ret = ArrayList<Validator>()
        when (type) {
            ValidatorFragment.TYPE_BONDED -> {
                val temp = list?.filter {
                    ValidatorFragment.TYPE_BONDED == it.status
                }
                if (temp != null && temp.isNotEmpty()) {
                    ret.addAll(temp)
                }
            }
            ValidatorFragment.TYPE_JAILED -> {
                val temp = list?.filter {
                    it.jailed == true
                }
                if (temp != null && temp.isNotEmpty()) {
                    ret.addAll(temp)
                }
            }
            else -> {
                if (list != null && list.isNotEmpty()) {
                    ret.addAll(list)
                }
            }
        }
        return ret
    }
}