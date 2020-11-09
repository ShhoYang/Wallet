package com.highstreet.wallet.gaojie.activity

import android.os.Handler
import com.highstreet.agent.utils.SPUtils
import com.highstreet.lib.common.AppManager
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.lib.view.dialog.ConfirmDialog
import com.highstreet.lib.view.dialog.ConfirmDialogListener
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.gaojie.AccountManager
import kotlinx.android.synthetic.main.g_activity_test.*

/**
 * @author Yang Shihao
 * @Date 2020/10/30
 */
class TestActivity : BaseActivity() {

    private var isTest = false
    private var temp = false

    override fun getLayoutId() = R.layout.g_activity_test

    override fun initView() {
        title = "设置"
        isTest = SPUtils.get(this, AccountManager.KEY_IS_TEST_CHAIN, false)
        temp = isTest


        rg.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbTest) {
                temp = true
            } else if (checkedId == R.id.rbMain) {
                temp = false
            }

            btnConfirm.isEnabled = temp != isTest
        }

        if (isTest) {
            rbTest.isChecked = true
        } else {
            rbMain.isChecked = true
        }

        RxView.click(btnConfirm) {
            ConfirmDialog(this).setMsg("确定修改为${if (temp) "测试网" else "主网"}?\n").setListener(object : ConfirmDialogListener {
                override fun confirm() {
                    toast("稍后手动重启")
                    SPUtils.put(this@TestActivity, AccountManager.KEY_IS_TEST_CHAIN, temp)
                    Handler().postDelayed({
                        AppManager.instance().exit()
                    }, 2000)
                }

                override fun cancel() {
                }

            }).show()
        }
    }
}