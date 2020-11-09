package com.highstreet.wallet.gaojie.vm

import android.arch.lifecycle.MutableLiveData
import com.highstreet.lib.viewmodel.BaseViewModel
import com.highstreet.wallet.gaojie.AccountManager
import com.highstreet.wallet.gaojie.model.WalletParams
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author Yang Shihao
 * @Date 2020/10/22
 */

class ImportWalletVM : BaseViewModel() {

    val resultLD = MutableLiveData<Boolean>()

    fun importWallet(name: String, walletParams: WalletParams) {
        Observable.create(ObservableOnSubscribe<Boolean> {
            val accountManager = AccountManager.instance()
            val account = AccountManager.generateAccount(walletParams.china, name, walletParams.entropyS, walletParams.address, walletParams.path, walletParams.newBip44)
            account.pushAlarm = true
            it.onNext(accountManager.addAccount(account, true))
            it.onComplete()
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    resultLD.value = it
                }, {
                    it.printStackTrace()
                    resultLD.value = null

                }).add()
    }
}