package com.highstreet.wallet.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.highstreet.wallet.R;
import com.highstreet.wallet.base.BaseActivity;
import com.highstreet.wallet.dao.Account;
import com.highstreet.wallet.dialog.Dialog_DisabledApp;
import com.highstreet.wallet.dialog.Dialog_NetworkError;
import com.highstreet.wallet.dialog.Dialog_Update;
import com.romainpiel.shimmer.ShimmerTextView;

public class IntroActivity extends BaseActivity implements View.OnClickListener {

    private ImageView       bgImg, bgImgGr;
    private ShimmerTextView logoTitle;
    private LinearLayout    bottomLayer1, bottomLayer2, bottomDetail, btnImportMnemonic, btnWatchAddress;
    private Button          mCreate, mImport;
    private LinearLayout    mNmemonicLayer, mWatchLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        bgImg               = findViewById(R.id.intro_bg);
        bgImgGr             = findViewById(R.id.intro_bg_gr);
        logoTitle           = findViewById(R.id.logo_title);
        bottomLayer1        = findViewById(R.id.bottom_layer1);
        bottomLayer2        = findViewById(R.id.bottom_layer2);
        bottomDetail        = findViewById(R.id.import_detail);
        btnImportMnemonic   = findViewById(R.id.btn_import_mnemonic);
        btnWatchAddress     = findViewById(R.id.btn_watch_address);
        mImport             = findViewById(R.id.btn_import);
        mCreate             = findViewById(R.id.btn_create);
        mNeedLeaveTime = false;

        mNmemonicLayer = findViewById(R.id.import_mnemonic_layer);
        mWatchLayer = findViewById(R.id.import_watch_layer);


        mImport.setOnClickListener(this);
        mCreate.setOnClickListener(this);
        btnImportMnemonic.setOnClickListener(this);
        btnWatchAddress.setOnClickListener(this);

//        WLog.w("UUID  " + new DeviceUuidFactory(this).getDeviceUuidS());
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
//            WLog.w("" + ((ActivityManager)getSystemService(Context.ACTIVITY_SERVICE)).isBackgroundRestricted());
//        }
//        WLog.w("FCM token Already : " + FirebaseInstanceId.getInstance().getInstanceId().getResult().getToken());
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        onCheckAppVersion();
    }

    private void onInitJob() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(getBaseDao().onSelectAccounts().size() == 0) {
                    onInitView();
                } else {
                    if(getBaseApplication().needShowLockScreen()) {
                        Intent intent = new Intent(IntroActivity.this, AppLockActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_bottom, R.anim.fade_out);
                    } else {
                        if (getIntent().getExtras() != null && getIntent().getExtras().getString("notifyto") != null) {
                            Account account = getBaseDao().onSelectExistAccount2(getIntent().getExtras().getString("notifyto"));
                            if (account != null) {
                                getBaseDao().setLastUser(account.id);
                                onStartMainActivity(2);
                                return;
                            }
                        }
                        onStartMainActivity(0);
                    }
                }
            }
        }, 2500);

    }


    private void onInitView() {
        Animation fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in5 );
        Animation fadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out5 );
        bgImgGr.startAnimation(fadeInAnimation);
        bgImg.startAnimation(fadeOutAnimation);

        final Animation mFadeInAni = AnimationUtils.loadAnimation(this, R.anim.fade_in2);
        Animation mFadeOutAni = AnimationUtils.loadAnimation(this, R.anim.fade_out2);
        mFadeOutAni.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }
            @Override
            public void onAnimationRepeat(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                bottomLayer2.setVisibility(View.VISIBLE);
                bottomLayer2.startAnimation(mFadeInAni);
            }
        });
        bottomLayer1.startAnimation(mFadeOutAni);


//        logoTitle.setVisibility(View.VISIBLE);
//        Shimmer shimmer = new Shimmer();
//        shimmer.setDuration(1500)
//                .setStartDelay(600)
//                .setDirection(Shimmer.ANIMATION_DIRECTION_LTR);
//        shimmer.start(logoTitle);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mImport)) {
            mImport.setVisibility(View.GONE);
            bottomDetail.setVisibility(View.VISIBLE);

            Animation fadein = AnimationUtils.loadAnimation(this, R.anim.fade_in4);
            mNmemonicLayer.startAnimation(fadein);
            mWatchLayer.startAnimation(fadein);


        } else if (v.equals(mCreate)) {
            startActivity(new Intent(IntroActivity.this, CreateActivity.class));

        } else if (v.equals(btnImportMnemonic)) {
            startActivity(new Intent(IntroActivity.this, RestoreActivity.class));

        } else if (v.equals(btnWatchAddress)) {
            startActivity(new Intent(IntroActivity.this, WatchingAccountAddActivity.class));
        }

    }


    private void onCheckAppVersion() {
        onInitJob();
//        ApiClient.getCosmostation(this).getVersion().enqueue(new Callback<ResVersionCheck>() {
//            @Override
//            public void onResponse(Call<ResVersionCheck> call, Response<ResVersionCheck> response) {
//                if (response.isSuccessful()) {
//                    if (!response.body().enable) {
//                        onDisableDialog();
//                    } else {
//                        if (response.body().version > BuildConfig.VERSION_CODE) {
//                            onUpdateDialog();
//                        } else {
//                            onInitJob();
//                        }
//                    }
//                } else {
//                    onNetworkDialog();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResVersionCheck> call, Throwable t) {
//                if(IS_SHOWLOG) { WLog.w("onCheckAppVersion onFailure " + t.getMessage()); }
//                onNetworkDialog();
//
//            }
//        });

    }


    private void onChangeImageWithFadeInAndOut( Context context, final ImageView imageView, final int resID ){

        final Animation fadeInAnimation = AnimationUtils.loadAnimation( context, R.anim.fade_in );
        Animation fadeOutAnimation = AnimationUtils.loadAnimation( context, R.anim.fade_out );
        fadeOutAnimation.setAnimationListener( new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.setImageResource( resID );
                imageView.startAnimation( fadeInAnimation );
            }
        });

        imageView.startAnimation( fadeOutAnimation );

    }


    private void onNetworkDialog() {
        Dialog_NetworkError dialog = new Dialog_NetworkError();
        dialog.setCancelable(false);
        getSupportFragmentManager().beginTransaction().add(dialog, "wait").commitNowAllowingStateLoss();
    }

    private void onDisableDialog() {
        Dialog_DisabledApp dialog = new Dialog_DisabledApp();
        dialog.setCancelable(false);
        getSupportFragmentManager().beginTransaction().add(dialog, "wait").commitNowAllowingStateLoss();

    }

    private void onUpdateDialog() {
        Dialog_Update dialog = new Dialog_Update();
        dialog.setCancelable(false);
        getSupportFragmentManager().beginTransaction().add(dialog, "wait").commitNowAllowingStateLoss();

    }


    public void onRetryVersionCheck() {
        onCheckAppVersion();
    }

    public void onTerminateApp() {
        finish();
    }

    public void onStartPlaystore() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + this.getPackageName()));
        startActivity(intent);
    }

}

