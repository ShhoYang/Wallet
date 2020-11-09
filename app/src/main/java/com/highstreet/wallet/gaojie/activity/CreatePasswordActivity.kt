package com.highstreet.wallet.gaojie.activity

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.view.View
import com.highstreet.lib.extensions.string
import com.highstreet.lib.fingerprint.FingerprintUtils
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.lib.view.dialog.ConfirmDialog
import com.highstreet.lib.view.dialog.ConfirmDialogListener
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.gaojie.AccountManager
import com.highstreet.wallet.gaojie.constant.Colors
import com.highstreet.wallet.gaojie.isPassword
import com.highstreet.wallet.gaojie.vm.CreatePasswordVM
import kotlinx.android.synthetic.main.g_activity_create_password.*

/**
 * @author Yang Shihao
 * @Date 2020/10/22
 */
class CreatePasswordActivity : BaseActivity(), View.OnFocusChangeListener {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(CreatePasswordVM::class.java)
    }

    override fun getLayoutId() = R.layout.g_activity_create_password

    override fun initView() {
        title = "设置密码"

        etPassword.onFocusChangeListener = this
        etConfirmPassword.onFocusChangeListener = this


        RxView.textChanges(etPassword, etConfirmPassword) {
            btnCreate.isEnabled = etPassword.string().isNotEmpty()
                    && etConfirmPassword.string().isNotEmpty()
        }

        RxView.click(btnCreate) {
            createPassword()
        }
    }

    private fun updateLineStyle(view: View, hasFocus: Boolean) {
        view.setBackgroundColor(if (hasFocus) Colors.editLineFocus else Colors.editLineBlur)
    }

    override fun initData() {
        viewModel.resultLD.observe(this, Observer {
            hideLoading()
            if (true == it) {
                toast("设置密码成功")
                setFingerprint()
            } else {
                toast("设置密码失败")
            }
        })
    }

    private fun createPassword() {

        val password = etPassword.string()
        val confirmPassword = etConfirmPassword.string()

        if (!password.isPassword()) {
            toast("输入的密码无效")
            return
        }

        if (password != confirmPassword) {
            toast("两次密码不一致")
            return
        }

        showLoading()
        viewModel.createPassword(password)
    }

    private fun setFingerprint() {
        if (FingerprintUtils.isAvailable(this)) {
            ConfirmDialog(this).setMsg("是否添加指纹验证").setListener(object : ConfirmDialogListener {
                override fun confirm() {
                    getFingerprint(useFingerprint = true, showUserPassword = false)?.authenticate()
                }

                override fun cancel() {
                    back()
                }
            }).show()

        } else {
            back()
        }
    }

    override fun onFingerprintAuthenticateSucceed() {
        AccountManager.instance().fingerprint = true
        back()
    }

    override fun onFingerprintCancel() {
        back()
    }

    private fun back() {
        setResult(RESULT_OK)
        finish()
    }

    override fun onFocusChange(v: View, hasFocus: Boolean) {
        when (v) {
            etPassword -> updateLineStyle(passwordLine, hasFocus)
            etConfirmPassword -> updateLineStyle(confirmPasswordLine, hasFocus)
        }
    }

    companion object {
        const val REQUEST_CODE_CREATE_PASSWORD = 101

        fun start(context: Activity) {
            context.startActivityForResult(Intent(context, CreatePasswordActivity::class.java), REQUEST_CODE_CREATE_PASSWORD)
        }
    }
}