package com.highstreet.wallet.gaojie.vm

import android.arch.lifecycle.MutableLiveData
import android.os.Handler
import com.highstreet.lib.viewmodel.BaseListViewModel
import com.highstreet.wallet.dao.Account
import com.highstreet.wallet.gaojie.AccountManager
import com.highstreet.wallet.gaojie.event.RefreshWalletListEvent
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author Yang Shihao
 * @Date 2020/10/22
 */
class WalletManageVM : BaseListViewModel<Account>() {

    private val handler = Handler()

    val deleteLD = MutableLiveData<Boolean>()

    override fun pageSize() = Int.MAX_VALUE

    override fun loadData(page: Int, onResponse: (ArrayList<Account>?) -> Unit) {
        handler.post { onResponse(AccountManager.instance().accounts) }
    }

    override fun onCreate() {
        super.onCreate()
        registerRxBus(RefreshWalletListEvent::class.java)
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    fun deleteAccount(account: Account) {
        Observable.create(ObservableOnSubscribe<Boolean> {
            it.onNext(AccountManager.instance().deleteAccount(account))
            it.onComplete()
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it && AccountManager.instance().accounts.isNotEmpty()) {
                        invalidate()
                    }
                    deleteLD.value = it
                }, {
                    it.printStackTrace()
                    deleteLD.value = false
                }).add()
    }
}
