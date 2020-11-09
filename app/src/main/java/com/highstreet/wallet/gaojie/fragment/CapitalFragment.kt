package com.highstreet.wallet.gaojie.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.text.TextUtils
import android.view.View
import com.highstreet.lib.extensions.gone
import com.highstreet.lib.extensions.visible
import com.highstreet.lib.ui.BaseFragment
import com.highstreet.lib.view.dialog.ConfirmDialog
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.gaojie.AccountManager
import com.highstreet.wallet.gaojie.StringUtils
import com.highstreet.wallet.gaojie.activity.ReceiveActivity
import com.highstreet.wallet.gaojie.activity.WalletManageActivity
import com.highstreet.wallet.gaojie.vm.CapitalVM
import kotlinx.android.synthetic.main.g_fragment_capital.*

/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */

class CapitalFragment : BaseFragment(), View.OnClickListener {

    private var amount = ""
    private var delegationAmount = ""

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(CapitalVM::class.java)
    }

    override fun getLayoutId() = R.layout.g_fragment_capital

    override fun initView() {

        tvChainName.text = AccountManager.instance().chainUpperCaseName

        baseRefreshLayout.setOnRefreshListener {
            loadData()
        }

        ivEye.isSelected = false
        RxView.click(tvWalletAddress, this)
        RxView.click(ivWalletAddress, this)
        RxView.click(ivEye, 300, this)

        RxView.click(ivSwitchWallet, this)
        RxView.click(ivTip, this)
        baseRefreshLayout.isRefreshing = true
    }

    override fun initData() {
        viewModel.amountLD.observe(this, Observer {
            val a = it?.getAmount()
            if (!TextUtils.isEmpty(a)) {
                amount = a!!
                updateUIStyle(ivEye.isSelected)
            }
            baseRefreshLayout.stopRefresh()
        })
        viewModel.delegationAmountLD.observe(this, Observer {
            val a = it ?: ""
            if (!TextUtils.isEmpty(a)) {
                delegationAmount = a!!
                updateUIStyle(ivEye.isSelected)
            }
            baseRefreshLayout.stopRefresh()
        })
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        viewModel.getAccountInfo()
        viewModel.getDelegationAmount()
    }

    private fun updateUIStyle(isSelected: Boolean) {
        val account = AccountManager.instance().account
        tvWalletName.text = account?.nickName
        tvWalletAddress.text = account?.address
        ivEye.isSelected = isSelected
        if (isSelected) {
            tvAmount.text = "****************"
            tvAvailableAmount.text = "********"
            tvDelegationAmount.text = "********"
            ivTip.gone()
        } else {
            tvAmount.text = StringUtils.pdip2DIP(amount)
            tvAvailableAmount.text = StringUtils.pdip2DIP(amount)
            tvDelegationAmount.text = StringUtils.pdip2DIP(delegationAmount)
            ivTip.visible()
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            tvWalletAddress, ivWalletAddress -> go(ReceiveActivity::class.java)
            ivEye -> updateUIStyle(!ivEye.isSelected)
            ivSwitchWallet -> go(WalletManageActivity::class.java)
            ivTip -> {
                activity?.let {
                    ConfirmDialog(it).setMsg("1DIP等于1,000,000,000,000pdip")
                            .hideCancel()
                            .show()
                }
            }
        }
    }
}