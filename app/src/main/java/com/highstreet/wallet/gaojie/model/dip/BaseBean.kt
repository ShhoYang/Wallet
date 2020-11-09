package com.highstreet.wallet.gaojie.model.dip

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */
data class BaseBean<T>(
        var error: String?,
        var height: String?,
        var result: T?)