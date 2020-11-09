package com.highstreet.wallet.gaojie.activity

import android.content.pm.PackageManager
import android.os.Build
import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.R
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.g_activity_carsh.*


/**
 * @author Yang Shihao
 * @Date 2019-07-20
 */
class CrashActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.g_activity_carsh

    override fun initView() {
        getToolbar()?.apply {
            showBack = false
            title = "SORRY"
        }
        RxView.click(btnRestart) {
            val config = CustomActivityOnCrash.getConfigFromIntent(intent)
            if (config != null) {
                CustomActivityOnCrash.restartApplication(this, config)
            }
        }
    }

    override fun initData() {
        val error = CustomActivityOnCrash.getStackTraceFromIntent(intent)
        val rxPermissions = RxPermissions(this)
        if (rxPermissions.isGranted(android.Manifest.permission.READ_PHONE_STATE)) {
            val content = getDeviceInfo() + error
            tvDetails.text = content
        } else {
            tvDetails.text = error
        }
    }

    private fun getDeviceInfo(): String {
        val stringBuffer = StringBuffer("")
        try {
            val packageManager = packageManager
            val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)

            if (packageInfo != null) {
                val versionName = if (packageInfo.versionName == null) "null" else packageInfo.versionName
                stringBuffer.append("versionName = ").append(versionName).append("\n")
                        .append("versionCode = ").append(packageInfo.versionCode).append("\n")
            }
            val fields = Build::class.java.declaredFields
            for (field in fields) {
                field.isAccessible = true
                stringBuffer.append(field.name).append(" = ").append(field.get(null).toString()).append("\n")
            }
            stringBuffer.append("\n\n")
            return stringBuffer.toString()
        } catch (e: PackageManager.NameNotFoundException) {
            return stringBuffer.toString()
        } catch (e: IllegalAccessException) {
            return stringBuffer.toString()
        }

    }
}