package com.highstreet.wallet.gaojie.model

import com.google.gson.Gson
import com.highstreet.wallet.base.BaseChain
import com.highstreet.wallet.gaojie.AccountManager
import com.highstreet.wallet.utils.WKey
import com.highstreet.wallet.utils.WUtil
import org.bitcoinj.crypto.DeterministicKey

/**
 * @author Yang Shihao
 * @Date 2020/10/22
 */


class WalletParams(
        val china: BaseChain,
        val path: Int,
        val newBip44: Boolean

) {

    var address: String = ""

    //   entropyS = WUtil.ByteArrayToHexString(value)
    var entropyS: String = ""
    var mnemonic: ArrayList<String> = ArrayList()
    var entropy: ByteArray = byteArrayOf()
        set(value) {
            field = value
            parseAddress(WKey.getRandomMnemonic(value))
        }

    /**
     * 推导地址
     */
    fun parseAddress(mnemonic: List<String>, entropy: ByteArray? = null) {
        this.mnemonic.clear()
        this.mnemonic.addAll(mnemonic)
        this.entropyS = WUtil.ByteArrayToHexString(entropy ?: WKey.toEntropy(this.mnemonic))
        val dKey: DeterministicKey = WKey.getKeyWithPathfromEntropy(china, entropyS, 0, newBip44)
        this.address = WKey.getDpAddress(china, dKey.publicKeyAsHex)
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }

    companion object {
        fun instance(): WalletParams {
            return WalletParams(AccountManager.instance().chain, 0, false)
        }
    }
}