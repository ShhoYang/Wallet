package com.highstreet.wallet.gaojie.activity

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.view.View
import com.highstreet.lib.extensions.string
import com.highstreet.lib.fingerprint.FingerprintUtils
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.crypto.CryptoHelper
import com.highstreet.wallet.gaojie.AccountManager
import com.highstreet.wallet.gaojie.constant.Colors
import com.highstreet.wallet.gaojie.constant.ExtraKey
import com.highstreet.wallet.gaojie.isAddress
import com.highstreet.wallet.gaojie.vm.ReceiveRewardVM
import kotlinx.android.synthetic.main.g_activity_receive_reward.*

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 *
 * 领取奖励
 */

class ReceiveRewardActivity : BaseActivity(), View.OnFocusChangeListener {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ReceiveRewardVM::class.java)
    }

    override fun getLayoutId() = R.layout.g_activity_receive_reward

    override fun initView() {
        title = "领取奖励"
        etReceiveAddress.onFocusChangeListener = this
        RxView.textChanges(etReceiveAddress) {
            btnConfirm.isEnabled = etReceiveAddress.string().isNotEmpty()
        }
        RxView.click(btnConfirm) {
            receive()
        }
    }

    private fun receive() {
        val validatorAddress = etValidatorAddress.string()
        if (!validatorAddress.isAddress()) {
            toast("验证人地址无效")
            return
        }
        val receiveAddress = etReceiveAddress.string()
        if (!receiveAddress.isAddress()) {
            toast("领取地址无效")
            return
        }

        getFingerprint(FingerprintUtils.isAvailable(this) && AccountManager.instance().fingerprint, true)?.authenticate()
    }

    private fun updateLineStyle(view: View, hasFocus: Boolean) {
        view.setBackgroundColor(if (hasFocus) Colors.editLineFocus else Colors.editLineBlur)
    }

    override fun initData() {
        val data = intent.getSerializableExtra(ExtraKey.SERIALIZABLE) as ArrayList<String>?
        if (data != null && data.size == 3) {
            etValidatorAddress.setText(data[0])
            etReceiveAddress.setText(data[1])
            etAmount.setText(data[2])
        }
        viewModel.resultLD.observe(this, Observer {
            hideLoading()
            toast(it?.second)
            if (true == it?.first) {
                to(MainActivity::class.java)
            }
        })
    }

    override fun onFingerprintAuthenticateSucceed() {
        showLoading()
        viewModel.receiveReward(etValidatorAddress.string(), etReceiveAddress.string())
    }

    override fun usePassword(password: String): Boolean {
        val userPassword = AccountManager.instance().password
        if (!CryptoHelper.verifyData(password, userPassword?.resource
                        ?: "", AccountManager.KEY_PASSWORD)) {
            return false
        }
        onFingerprintAuthenticateSucceed()
        return true
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        when (v) {
            etReceiveAddress -> updateLineStyle(receiveAddressLine, hasFocus)
        }
    }

    companion object {
        fun start(context: Context, validatorAddress: String, delegatorAddress: String, reword: String) {
            val intent = Intent(context, ReceiveRewardActivity::class.java)
            intent.putExtra(ExtraKey.SERIALIZABLE, arrayListOf(validatorAddress, delegatorAddress, reword))
            context.startActivity(intent)
        }
    }
}