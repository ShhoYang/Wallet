package com.highstreet.wallet.gaojie

import org.bitcoinj.crypto.MnemonicCode
import org.bitcoinj.crypto.MnemonicException.MnemonicLengthException
import java.security.SecureRandom

/**
 * @author Yang Shihao
 * @Date 1/18/21
 */
object KeyUtils {

    /**
     * 生成熵
     *
     * getEntropy
     */
    fun generateEntropy(): ByteArray {
        val byteArray = ByteArray(32)
        SecureRandom().nextBytes(byteArray)
        return byteArray
    }

    /**
     * 助记词转熵
     *
     * toEntropy
     */
    fun mnemonic2Entropy(mnemonic: List<String>): ByteArray? {
        return try {
            MnemonicCode.INSTANCE.toEntropy(mnemonic)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 熵转助记词
     *
     * getRandomMnemonic
     */
    fun entropy2Mnemonic(entropy: ByteArray): List<String>? {
        return try {
            MnemonicCode.INSTANCE.toMnemonic(entropy)
        } catch (e: MnemonicLengthException) {
            e.printStackTrace()
            null
        }
    }
}