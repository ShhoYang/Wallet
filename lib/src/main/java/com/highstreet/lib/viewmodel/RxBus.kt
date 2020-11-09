package com.highstreet.lib.viewmodel

import io.reactivex.Flowable
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor

/**
 * @author Yang Shihao
 * @Date 2020/10/23
 */
class RxBus private constructor() {

    private val bus: FlowableProcessor<Any> = PublishProcessor.create<Any>().toSerialized()

    fun send(obj: Any) {
        bus.onNext(obj)
    }

    fun <T> register(cls: Class<T>): Flowable<T> {
        return bus.ofType(cls)
    }

    fun register(): Flowable<Any> {
        return bus
    }

    fun hasSubscribers(): Boolean {
        return bus.hasSubscribers()
    }

    fun unregisterAll() {
        bus.onComplete()
    }

    companion object {
        private var instance: RxBus? = null

        @Synchronized
        fun instance(): RxBus {
            if (instance == null) {
                instance = RxBus()
            }
            return instance!!
        }
    }
}
