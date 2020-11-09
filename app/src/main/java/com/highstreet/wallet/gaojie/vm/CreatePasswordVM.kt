package com.highstreet.wallet.gaojie.vm

import android.arch.lifecycle.MutableLiveData
import com.highstreet.lib.viewmodel.BaseViewModel
import com.highstreet.wallet.crypto.CryptoHelper
import com.highstreet.wallet.dao.Password
import com.highstreet.wallet.gaojie.AccountManager
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * @author Yang Shihao
 * @Date 2020/10/22
 */

class CreatePasswordVM : BaseViewModel() {

    val resultLD = MutableLiveData<Boolean>()

    fun createPassword(password: String) {
        Observable.create(ObservableOnSubscribe<Boolean> {
            val accountManager = AccountManager.instance()
            val newPw = Password(CryptoHelper.signData(password, AccountManager.KEY_PASSWORD))
            val ret: Long = accountManager.dao().onInsertPassword(newPw)
            if (ret > 0) {
                it.onNext(true)
                AccountManager.instance().password = newPw
            } else {
                it.onNext(false)
            }

            it.onComplete()
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    resultLD.value = it
                }, {
                    it.printStackTrace()
                    resultLD.value = false
                }).add()
    }
}