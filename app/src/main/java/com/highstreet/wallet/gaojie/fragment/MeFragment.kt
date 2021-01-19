package com.highstreet.wallet.gaojie.fragment

import android.view.View
import com.highstreet.lib.adapter.OnItemClickListener
import com.highstreet.lib.extensions.init
import com.highstreet.lib.extensions.visibility
import com.highstreet.lib.fingerprint.DialogParams
import com.highstreet.lib.fingerprint.listener.FingerprintCallback
import com.highstreet.lib.fingerprint.FingerprintM
import com.highstreet.lib.fingerprint.FingerprintUtils
import com.highstreet.lib.fingerprint.IFingerprint
import com.highstreet.lib.ui.BaseFragment
import com.highstreet.lib.view.dialog.ConfirmDialog
import com.highstreet.lib.view.dialog.ConfirmDialogListener
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.gaojie.AccountManager
import com.highstreet.wallet.gaojie.activity.AboutActivity
import com.highstreet.wallet.gaojie.activity.TransactionActivity
import com.highstreet.wallet.gaojie.activity.TransactionRecordActivity
import com.highstreet.wallet.gaojie.activity.WalletManageActivity
import com.highstreet.wallet.gaojie.adapter.MenuAdapter
import com.highstreet.wallet.gaojie.model.dip.Menu
import kotlinx.android.synthetic.main.g_fragment_me.*


/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */

class MeFragment : BaseFragment(), FingerprintCallback, ConfirmDialogListener {

    private var fingerprintM: IFingerprint? = null

    private var setFingerprint = false

    override fun getLayoutId() = R.layout.g_fragment_me

    override fun initView() {

        val isFingerprint = FingerprintUtils.isHardwareDetected(context)

        clFingerprint.visibility(isFingerprint)

        val menuList = arrayListOf(
                Menu("钱包管理", R.mipmap.my_wallet, WalletManageActivity::class.java),
                Menu.wide(),
                Menu("关于我们", R.mipmap.my_friend, AboutActivity::class.java)
        )
        if (isFingerprint) {
            menuList.add(0, Menu.wide())
        }
        val menuAdapter = MenuAdapter(menuList)
        menuAdapter.itemClickListener = object : OnItemClickListener<Menu> {

            override fun itemClicked(view: View, item: Menu, position: Int) {
                if (item.cls == null) {

                } else {
                    this@MeFragment.to(item.cls)
                }
            }
        }

        baseRecyclerView.init(menuAdapter)

        setFingerprint = AccountManager.instance().fingerprint
        updateSwitchStyle()

        RxView.click(ivSwitch) {
            if (setFingerprint) {
                activity?.let {
                    ConfirmDialog(it).setMsg("确定取消指纹验证？").setListener(this).show()
                }
            } else if (FingerprintUtils.hasEnrolledFingerprints(context)) {
                getFingerprint()?.authenticate()
            } else {
                toast("没有录入指纹，请先到系统设置录入指纹")
            }
        }
    }

    private fun getFingerprint(): IFingerprint? {
        if (null == fingerprintM) {
            activity?.let {
                val b = FingerprintUtils.isAvailable(it)
                fingerprintM = FingerprintM()
                fingerprintM!!.init(it,
                        true,
                        this,
                        DialogParams(useFingerprint = true, showUserPassword = false)
                )
            }
        }

        return fingerprintM
    }

    override fun onStop() {
        super.onStop()
        fingerprintM?.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fingerprintM?.onDestroy()
    }

    /**
     * 指纹监听
     */
    override fun onFingerprintAuthenticateSucceed() {
        setFingerprint = !setFingerprint
        AccountManager.instance().fingerprint = setFingerprint
        updateSwitchStyle()
    }

    override fun onFingerprintCancel() {

    }

    override fun usePassword(password: String): Boolean? {
        return true
    }

    private fun updateSwitchStyle() {
        if (setFingerprint) {
            ivSwitch.setImageResource(R.mipmap.icon_switch_open)
        } else {
            ivSwitch.setImageResource(R.mipmap.icon_switch_close)
        }
    }

    companion object {
        private const val TAG = "--MeFragment--"
    }

    override fun confirm() {
        getFingerprint()?.authenticate()
    }

    override fun cancel() {

    }
}