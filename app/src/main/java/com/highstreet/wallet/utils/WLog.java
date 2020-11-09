package com.highstreet.wallet.utils;

import android.util.Log;

import com.highstreet.wallet.base.BaseConstant;

public class WLog {

    public static void e(String msg) {
        if (BaseConstant.IS_SHOWLOG) {
            Log.e(BaseConstant.LOG_TAG, msg + "\n");
        }
    }

    public static void w(String msg) {
        if (BaseConstant.IS_SHOWLOG) {
            Log.w(BaseConstant.LOG_TAG, msg+ "\n");
        }
    }
}
