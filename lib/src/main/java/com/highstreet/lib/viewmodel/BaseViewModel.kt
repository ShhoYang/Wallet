package com.highstreet.lib.viewmodel

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ViewModel
import com.socks.library.KLog
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BaseViewModel : ViewModel(), LifecycleObserver {

    private var tag = "BaseViewModel_${javaClass.simpleName}"

    var needRefreshInResume = false

    private val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }

    /**
     * 注册RxBus,和needRefreshInResume配合使用
     */
    fun <T> registerRxBus(cls: Class<T>) {
        RxBus.instance().register(cls).subscribe({
            KLog.d(tag, "registerRxBus#subscribe")
            needRefreshInResume = true
        }, {}).add()
    }

    fun Disposable.add() {
        compositeDisposable.add(this)
    }

    open fun refreshInResume() {

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    protected open fun onCreate() {
        KLog.d(tag, "onCreate")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected open fun onStart() {
        KLog.d(tag, "onStart")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected open fun onResume() {
        KLog.d(tag, "onResume")
        if (needRefreshInResume) {
            needRefreshInResume = false
            refreshInResume()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected open fun onPause() {
        KLog.d(tag, "onPause")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected open fun onStop() {
        KLog.d(tag, "onStop")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected open fun onDestroy() {
        KLog.d(tag, "onDestroy")
    }

    override fun onCleared() {
        super.onCleared()
        KLog.d(tag, "onCleared")
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }
}
