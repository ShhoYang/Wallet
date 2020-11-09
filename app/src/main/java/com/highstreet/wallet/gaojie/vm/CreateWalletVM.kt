package com.highstreet.wallet.gaojie.vm

import android.arch.lifecycle.MutableLiveData
import com.highstreet.lib.viewmodel.BaseViewModel
import com.highstreet.wallet.BuildConfig
import com.highstreet.wallet.gaojie.AccountManager
import com.highstreet.wallet.gaojie.http.ApiService
import com.highstreet.wallet.gaojie.http.subscribeBy
import com.highstreet.wallet.gaojie.model.WalletParams
import com.highstreet.wallet.utils.WKey
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */

class CreateWalletVM : BaseViewModel() {

    private val mnemonic = ArrayList<String>()

    val resultLD = MutableLiveData<Boolean>()

    private var accountManager = AccountManager.instance()

    fun createWallet(name: String, walletParams: WalletParams) {
        Observable.create(ObservableOnSubscribe<Boolean> {
            val account = AccountManager.generateAccount(walletParams.china, name, walletParams.entropyS, walletParams.address, walletParams.path, walletParams.newBip44)
            if (accountManager.addAccount(account, true)) {
                val randomMnemonic = WKey.getRandomMnemonic(walletParams.entropy)
                mnemonic.clear()
                mnemonic.addAll(randomMnemonic)
                it.onNext(true)
            } else {
                it.onNext(false)
            }
            it.onComplete()
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    if (it) {
                        test()
                    } else {
                        resultLD.value = false
                    }
                }, {
                    it.printStackTrace()
                    resultLD.value = false

                }).add()
    }

    /**
     * 水龙头
     */
    private fun test() {
        if (!BuildConfig.testnet) {
            resultLD.value = true
            return
        }

        ApiService.getDipApi().test("https://docs.dippernetwork.com/dip/get_token?" + accountManager.address).subscribeBy({
            resultLD.value = true
        }, {
            resultLD.value = true
        }).add()
    }
}