package com.highstreet.wallet.gaojie

import android.text.TextUtils
import com.highstreet.agent.utils.SPUtils
import com.highstreet.wallet.BuildConfig
import com.highstreet.wallet.base.BaseApplication
import com.highstreet.wallet.base.BaseChain
import com.highstreet.wallet.crypto.CryptoHelper
import com.highstreet.wallet.crypto.EncResult
import com.highstreet.wallet.dao.Account
import com.highstreet.wallet.dao.Password

/**
 * @author Yang Shihao
 * @Date 2020/10/15
 */
class AccountManager private constructor() {

    var chain = BaseChain.DIP_MAIN

    /**
     * 所有账户
     */
    var accounts = ArrayList<Account>()

    /**
     * 当前账户
     */
    var account: Account? = null

    /**
     * 密码
     */
    var password: Password? = null

    /**
     * 当前账户地址
     */
    var address: String = ""
        get() {
            return account?.address ?: ""
        }

    /**
     * 当前链名大写
     */
    var chainUpperCaseName: String = ""
        get() {
            return if (TextUtils.isEmpty(account?.baseChain)) {
                ""
            } else {
                account!!.baseChain.replace("-", " ").toUpperCase()
            }
        }

    /**
     * 是否开始指纹验证
     */
    var fingerprint = false
        set(value) {
            field = value
            SPUtils.put(BaseApplication.instance, KEY_FINGERPRINT, value)
        }

    fun dao() = BaseApplication.instance.baseDao

    /**
     * 子线程执行
     */
    fun init() {
//        var isTest = SPUtils.get(BaseApplication.instance, KEY_IS_TEST_CHAIN, false)
        var isTest = BuildConfig.testnet
        chain = if (isTest) BaseChain.DIP_TEST else BaseChain.DIP_MAIN
        initAccounts()
        password = dao().onSelectPassword()
        fingerprint = SPUtils.get(BaseApplication.instance, KEY_FINGERPRINT, false)
    }

    /**
     * @return 是否插入成功
     */
    fun addAccount(account: Account, isLast: Boolean): Boolean {
        val dao = dao()
        val ret = dao.onInsertAccount(account)

        if (ret < 1) {
            return false
        }

        if (isLast) {
            dao.setLastUser(ret)
        }
        initAccounts()
        return true
    }

    /**
     * @return 删除插入成功
     */
    fun deleteAccount(account: Account): Boolean {
        val dao = dao()
        val ret = dao.onDeleteAccount("${account.id}")

        if (!ret) {
            return false
        }

        initAccounts()

        return true
    }

    fun setLastUser() {
        val id = account?.id ?: -1
        if (id > 0) {
            dao().setLastUser(id)
        }
    }

    private fun initAccounts() {
        val dao = dao()
        val accounts = dao.onSelectAccounts()
        this.accounts.clear()
        this.accounts.addAll(accounts)
        if (this.accounts.isNotEmpty()) {
            account = dao.onSelectAccount(dao.lastUser)
            if (account == null || account?.baseChain != chain.chain) {
                account = accounts[0]
                setLastUser()
            }
        } else {
            account = Account()
        }

        address = account?.address ?: ""

//        val a = account!!
//        val s = getEntropy(a)
//        val list = WKey.getRandomMnemonic(WUtil.HexStringToByteArray(s))
//        KLog.d("----------------------", "{\"address\":\"${a.address}\",\"mnemonic\":\"${list.joinToString(" ")}\"}")
    }

    companion object {

        private const val KEY_FINGERPRINT = "KEY_FINGERPRINT"
        const val KEY_IS_TEST_CHAIN = "KEY_IS_TEST_CHAIN"
        const val KEY_PASSWORD = "PASSWORD_KEY"
        const val KEY_MNEMONIC = "MNEMONIC_KEY"

        private var instance: AccountManager? = null

        @Synchronized
        fun instance(): AccountManager {
            if (instance == null) {
                instance = AccountManager()
            }
            return instance!!
        }

        fun getEntropy(account: Account): String {
            return CryptoHelper.doDecryptData(KEY_MNEMONIC + account.uuid, account.resource, account.spec)
        }

        fun generateAccount(china: BaseChain, name: String, entropy: String, address: String, path: Int, newBip44: Boolean): Account {
            val account = Account.getNewInstance()
            val encR: EncResult = CryptoHelper.doEncryptData(KEY_MNEMONIC + account.uuid, entropy, false)
            account.address = address
            account.baseChain = china.chain
            account.hasPrivateKey = true
            account.resource = encR.encDataString
            account.spec = encR.ivDataString
            account.fromMnemonic = true
            account.path = path.toString()
            account.msize = 24
            account.importTime = System.currentTimeMillis()
            account.newBip44 = newBip44
            account.nickName = name
            return account
        }
    }
}