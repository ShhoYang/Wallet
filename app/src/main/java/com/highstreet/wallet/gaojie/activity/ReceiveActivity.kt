package com.highstreet.wallet.gaojie.activity

import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import com.highstreet.lib.ui.BaseActivity
import com.highstreet.lib.utils.DisplayUtils
import com.highstreet.lib.view.listener.RxView
import com.highstreet.wallet.R
import com.highstreet.wallet.gaojie.AccountManager
import com.highstreet.wallet.gaojie.copy
import kotlinx.android.synthetic.main.g_activity_receive.*


/**
 * @author Yang Shihao
 * @Date 2020/10/16
 */

class ReceiveActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.g_activity_receive

    override fun initView() {
        super.initView()
        title = "收款"
        tvChainName.text = AccountManager.instance().chainUpperCaseName
        val address = AccountManager.instance().address
        tvAddress.text = address

        RxView.click(btnCopy) {
            address.copy(this)
        }
        val width = DisplayUtils.getScreenWidth(this) / 2
        ivQr.setImageBitmap(generateQRCode(address, width, width))
    }

    private fun generateQRCode(content: String, width: Int, height: Int): Bitmap? {
        val qrCodeWriter = QRCodeWriter()
        val hints: MutableMap<EncodeHintType, String?> = HashMap()
        hints[EncodeHintType.CHARACTER_SET] = "UTF-8"
        try {
            val encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints)
            val pixels = IntArray(width * height)
            for (i in 0 until height) {
                for (j in 0 until width) {
                    if (encode[j, i]) {
                        pixels[i * width + j] = 0x000000
                    } else {
                        pixels[i * width + j] = 0XFFFFFF
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
        return null
    }
}