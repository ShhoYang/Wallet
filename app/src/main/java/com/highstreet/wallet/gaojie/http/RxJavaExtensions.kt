package com.highstreet.wallet.gaojie.http

import com.highstreet.lib.utils.T
import com.highstreet.wallet.base.BaseApplication
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

fun <D> Observable<D>.subscribeBy(
        onResponse: (D) -> Unit,
        onFailure: ((String) -> Unit)? = null,
        toast: Boolean = true
): Disposable {
    return subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onResponse(it)
            }, {
                it.printStackTrace()
                if (onFailure != null && it.message != null) {
                    onFailure(it.message!!)
                }
                if (toast) {
                    T.short(BaseApplication.instance, "网络不给力")
                }
            })
}
