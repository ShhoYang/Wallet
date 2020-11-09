package com.highstreet.wallet.base;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.highstreet.wallet.BuildConfig;
import com.highstreet.wallet.gaojie.activity.CrashActivity;
import com.highstreet.wallet.gaojie.activity.WelcomeActivity;
import com.highstreet.wallet.utils.DeviceUuidFactory;
import com.socks.library.KLog;
import com.squareup.picasso.Picasso;
import com.tencent.bugly.crashreport.CrashReport;

import cat.ereza.customactivityoncrash.config.CaocConfig;

public class BaseApplication extends Application {

    private BaseData mBaseData;
    private AppStatus mAppStatus;

    public static BaseApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        new DeviceUuidFactory(this);
        CrashReport.initCrashReport(getApplicationContext(), "88dfb47f91", false);
        registerActivityLifecycleCallbacks(new LifecycleCallbacks());

        Picasso.Builder builder = new Picasso.Builder(this);
        Picasso built = builder.build();
        built.setIndicatorsEnabled(false);
        Picasso.setSingletonInstance(built);
        CaocConfig.Builder
                .create()
                .errorActivity(CrashActivity.class)
                .restartActivity(WelcomeActivity.class)
                .apply();
        KLog.init(BuildConfig.testnet);
    }

    public BaseData getBaseDao() {
        if (mBaseData == null)
            mBaseData = new BaseData(this);
        return mBaseData;
    }

    public boolean isReturnedForground() {
        return mAppStatus.ordinal() == AppStatus.RETURNED_TO_FOREGROUND.ordinal();
    }

    public boolean needShowLockScreen() {
        if (!isReturnedForground() ||
                !getBaseDao().onHasPassword() ||
                !getBaseDao().getUsingAppLock() ||
                (getBaseDao().onSelectAccounts().size() <= 0)) return false;

        if (getBaseDao().getAppLockTriggerTime() == 0) {
            return true;
        } else if (getBaseDao().getAppLockTriggerTime() == 1) {
            if ((getBaseDao().getAppLockLeaveTime() + BaseConstant.CONSTANT_10S) >= System.currentTimeMillis())
                return false;

        } else if (getBaseDao().getAppLockTriggerTime() == 2) {
            if ((getBaseDao().getAppLockLeaveTime() + BaseConstant.CONSTANT_30S) >= System.currentTimeMillis())
                return false;

        } else if (getBaseDao().getAppLockTriggerTime() == 3) {
            if ((getBaseDao().getAppLockLeaveTime() + BaseConstant.CONSTANT_M) >= System.currentTimeMillis())
                return false;

        }
        return true;
    }


    public enum AppStatus {
        BACKGROUND,
        RETURNED_TO_FOREGROUND,
        FOREGROUND;
    }

    public class LifecycleCallbacks implements ActivityLifecycleCallbacks {

        private int running = 0;

        @Override
        public void onActivityStarted(Activity activity) {
            if (++running == 1) {
                mAppStatus = AppStatus.RETURNED_TO_FOREGROUND;
            } else if (running > 1) {
                mAppStatus = AppStatus.FOREGROUND;
            }

        }

        @Override
        public void onActivityStopped(Activity activity) {
            if (--running == 0) {
                mAppStatus = AppStatus.BACKGROUND;
            }
        }

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        }

        @Override
        public void onActivityResumed(Activity activity) {
        }

        @Override
        public void onActivityPaused(Activity activity) {
        }


        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
        }
    }
}
