package com.highstreet.wallet.base;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.highstreet.wallet.R;
import com.highstreet.wallet.activities.AppLockActivity;
import com.highstreet.wallet.activities.ClaimIncentiveActivity;
import com.highstreet.wallet.activities.HtlcSendActivity;
import com.highstreet.wallet.activities.IntroActivity;
import com.highstreet.wallet.activities.MainActivity;
import com.highstreet.wallet.activities.PasswordCheckActivity;
import com.highstreet.wallet.activities.PasswordSetActivity;
import com.highstreet.wallet.activities.RestoreActivity;
import com.highstreet.wallet.crypto.CryptoHelper;
import com.highstreet.wallet.dao.Account;
import com.highstreet.wallet.dao.Balance;
import com.highstreet.wallet.dao.BnbToken;
import com.highstreet.wallet.dao.BondingState;
import com.highstreet.wallet.dao.IrisToken;
import com.highstreet.wallet.dao.Reward;
import com.highstreet.wallet.dao.UnBondingState;
import com.highstreet.wallet.dialog.Dialog_Buy_Select_Fiat;
import com.highstreet.wallet.dialog.Dialog_Buy_Without_Key;
import com.highstreet.wallet.dialog.Dialog_Push_Enable;
import com.highstreet.wallet.dialog.Dialog_ShareType;
import com.highstreet.wallet.dialog.Dialog_Wait;
import com.highstreet.wallet.dialog.Dialog_WatchMode;
import com.highstreet.wallet.model.type.Validator;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.res.ResBnbFee;
import com.highstreet.wallet.network.res.ResCdpOwnerStatus;
import com.highstreet.wallet.network.res.ResCdpParam;
import com.highstreet.wallet.network.res.ResCgcTic;
import com.highstreet.wallet.network.res.ResCmcTic;
import com.highstreet.wallet.network.res.ResIovConfig;
import com.highstreet.wallet.network.res.ResIovFee;
import com.highstreet.wallet.network.res.ResKavaIncentiveParam;
import com.highstreet.wallet.network.res.ResKavaIncentiveReward;
import com.highstreet.wallet.network.res.ResKavaMarketPrice;
import com.highstreet.wallet.network.res.ResKavaPriceParam;
import com.highstreet.wallet.network.res.ResLcdIrisPool;
import com.highstreet.wallet.network.res.ResLcdIrisReward;
import com.highstreet.wallet.network.res.ResOkDeposit;
import com.highstreet.wallet.network.res.ResOkTokenList;
import com.highstreet.wallet.network.res.ResOkWithdraw;
import com.highstreet.wallet.network.res.ResStakingPool;
import com.highstreet.wallet.task.FetchTask.AccountInfoTask;
import com.highstreet.wallet.task.FetchTask.AllValidatorInfoTask;
import com.highstreet.wallet.task.FetchTask.BnbMiniTokenListTask;
import com.highstreet.wallet.task.FetchTask.BnbTokenListTask;
import com.highstreet.wallet.task.FetchTask.BondingStateTask;
import com.highstreet.wallet.task.FetchTask.IrisPoolTask;
import com.highstreet.wallet.task.FetchTask.IrisRewardTask;
import com.highstreet.wallet.task.FetchTask.IrisTokenListTask;
import com.highstreet.wallet.task.FetchTask.KavaCdpByOwnerTask;
import com.highstreet.wallet.task.FetchTask.KavaCdpParamTask;
import com.highstreet.wallet.task.FetchTask.KavaIncentiveParamTask;
import com.highstreet.wallet.task.FetchTask.KavaIncentiveRewardTask;
import com.highstreet.wallet.task.FetchTask.KavaMarketPriceTask;
import com.highstreet.wallet.task.FetchTask.KavaPriceParamTask;
import com.highstreet.wallet.task.FetchTask.MoonPayTask;
import com.highstreet.wallet.task.FetchTask.OkAccountTokenTask;
import com.highstreet.wallet.task.FetchTask.OkDepositTask;
import com.highstreet.wallet.task.FetchTask.OkTokenListTask;
import com.highstreet.wallet.task.FetchTask.OkWithdrawTask;
import com.highstreet.wallet.task.FetchTask.PushUpdateTask;
import com.highstreet.wallet.task.FetchTask.StarNameConfigTask;
import com.highstreet.wallet.task.FetchTask.StarNameFeeTask;
import com.highstreet.wallet.task.FetchTask.UnBondingStateTask;
import com.highstreet.wallet.task.FetchTask.UnbondedValidatorInfoTask;
import com.highstreet.wallet.task.FetchTask.UnbondingValidatorInfoTask;
import com.highstreet.wallet.task.SingleFetchTask.SingleInflationTask;
import com.highstreet.wallet.task.SingleFetchTask.SingleProvisionsTask;
import com.highstreet.wallet.task.SingleFetchTask.SingleRewardTask;
import com.highstreet.wallet.task.SingleFetchTask.SingleStakingPoolTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.FetchCallBack;
import com.highstreet.wallet.utils.WDp;
import com.highstreet.wallet.utils.WLog;
import com.highstreet.wallet.utils.WUtil;
import com.shasin.notificationbanner.Banner;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.highstreet.wallet.base.BaseChain.BAND_MAIN;
import static com.highstreet.wallet.base.BaseChain.CERTIK_TEST;
import static com.highstreet.wallet.base.BaseChain.COSMOS_MAIN;
import static com.highstreet.wallet.base.BaseChain.IOV_MAIN;
import static com.highstreet.wallet.base.BaseChain.IOV_TEST;
import static com.highstreet.wallet.base.BaseChain.KAVA_MAIN;
import static com.highstreet.wallet.base.BaseChain.KAVA_TEST;
import static com.highstreet.wallet.base.BaseConstant.FEE_BNB_SEND;
import static com.highstreet.wallet.base.BaseConstant.TASK_FETCH_BNB_FEES;
import static com.highstreet.wallet.base.BaseConstant.TASK_FETCH_KAVA_CDP_OWENER;
import static com.highstreet.wallet.base.BaseConstant.TASK_FETCH_KAVA_INCENTIVE_PARAM;
import static com.highstreet.wallet.base.BaseConstant.TASK_FETCH_KAVA_INCENTIVE_REWARD;
import static com.highstreet.wallet.base.BaseConstant.TASK_FETCH_KAVA_PRICE_PARAM;
import static com.highstreet.wallet.base.BaseConstant.TASK_FETCH_KAVA_TOKEN_PRICE;
import static com.highstreet.wallet.base.BaseConstant.TASK_FETCH_OK_ACCOUNT_TOKEN;
import static com.highstreet.wallet.base.BaseConstant.TASK_FETCH_OK_DEPOSIT;
import static com.highstreet.wallet.base.BaseConstant.TASK_FETCH_OK_TOKEN_LIST;
import static com.highstreet.wallet.base.BaseConstant.TASK_FETCH_OK_WITHDRAW;
import static com.highstreet.wallet.base.BaseConstant.TASK_FETCH_STARNAME_CONFIG;
import static com.highstreet.wallet.base.BaseConstant.TASK_FETCH_STARNAME_FEE;
import static com.highstreet.wallet.base.BaseConstant.TOKEN_BNB;

public class BaseActivity extends AppCompatActivity implements TaskListener {

    protected BaseApplication               mApplication;
    protected BaseData                      mData;
    protected Dialog_Wait                   mDialogWait;
    protected boolean                       mNeedLeaveTime = true;

    public View                             mRootview;
    public Account                          mAccount;
    public BaseChain                        mBaseChain;
    public ArrayList<Validator>             mOtherValidators = new ArrayList<>();
    public ArrayList<Validator>             mTopValidators = new ArrayList<>();
    public ArrayList<Validator>             mMyValidators = new ArrayList<>();
    public ArrayList<Validator>             mAllValidators = new ArrayList<>();
    public ArrayList<Balance>               mBalances = new ArrayList<>();
    public ArrayList<BondingState>          mBondings = new ArrayList<>();
    public ArrayList<UnBondingState>        mUnbondings = new ArrayList<>();
    public ArrayList<Reward>                mRewards = new ArrayList<>();


    public ResStakingPool                   mStakingPool;
    public ResLcdIrisPool                   mIrisStakingPool;
    public BigDecimal                       mInflation = BigDecimal.ZERO;
    public BigDecimal                       mProvisions = BigDecimal.ZERO;

    public ResLcdIrisReward                 mIrisReward;
    public ArrayList<IrisToken>             mIrisTokens = new ArrayList<>();

    protected int                           mTaskCount;
    private FetchCallBack                   mFetchCallback;


    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            onDisplayNotification(intent);
        }
    };

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRootview  = findViewById(android.R.id.content);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!(this instanceof PasswordSetActivity) && !(this instanceof PasswordCheckActivity) && !(this instanceof IntroActivity)) {
            if(getBaseApplication().needShowLockScreen()) {
                Intent intent = new Intent(BaseActivity.this, AppLockActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.fade_out);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("pushAlarm"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mNeedLeaveTime) {
            getBaseDao().setAppLockLeaveTime();
        }

    }

    public BaseApplication getBaseApplication() {
        if (mApplication == null)
            mApplication = (BaseApplication) getApplication();
        return mApplication;
    }

    public BaseData getBaseDao() {
        if (mData == null)
            mData = getBaseApplication().getBaseDao();
        return mData;
    }

    public void onShowWaitDialog() {
        mDialogWait = new Dialog_Wait();
        mDialogWait.setCancelable(false);
        getSupportFragmentManager().beginTransaction().add(mDialogWait, "wait").commitNowAllowingStateLoss();
    }

    public void onHideWaitDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("wait");
        if (prev != null) {
            ft.remove(prev).commitNowAllowingStateLoss();
        }
    }

    public void onHideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        View v = getCurrentFocus();
        if (v == null) {
            v = new View(getBaseContext());
        }
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        v.clearFocus();
    }

    public void onStartMainActivity(int page) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("page", page);
        startActivity(intent);
    }



    public void onStartHTLCSendActivity(String sendDenom) {
//        WLog.w("onStartHTLCSendActivity " + mBaseChain.getChain() + " " + sendDenom);
        if (mAccount == null) return;
        if (!mAccount.hasPrivateKey) {
            Dialog_WatchMode add = Dialog_WatchMode.newInstance();
            add.setCancelable(true);
            getSupportFragmentManager().beginTransaction().add(add, "dialog").commitNowAllowingStateLoss();
            return;
        }

        boolean hasbalance = true;
        if (mBaseChain.equals(BaseChain.BNB_MAIN) || mBaseChain.equals(BaseChain.BNB_TEST)) {
            if (WDp.getAvailableCoin(mAccount.balances, TOKEN_BNB).compareTo(new BigDecimal(FEE_BNB_SEND)) <= 0) {
                hasbalance  = false;
            }
        }
        if (!hasbalance) {
            Toast.makeText(getBaseContext(), R.string.error_not_enough_budget_bep3, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(getBaseContext(), HtlcSendActivity.class);
        intent.putExtra("toSwapDenom", sendDenom);
        startActivity(intent);
    }

    public void onStartIncentiveClaim() {
        if (mAccount == null) return;
        if (!mAccount.hasPrivateKey) {
            Dialog_WatchMode add = Dialog_WatchMode.newInstance();
            add.setCancelable(true);
            getSupportFragmentManager().beginTransaction().add(add, "dialog").commitNowAllowingStateLoss();
            return;
        }
        Intent intent = new Intent(getBaseContext(), ClaimIncentiveActivity.class);
        startActivity(intent);

    }

    public void onChoiceNet(BaseChain chain) { }

    public void onShare(boolean isText, String address) {
        if(isText) {
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, address);
            shareIntent.setType("text/plain");
            startActivity(Intent.createChooser(shareIntent, "send"));

        } else {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            try {
                final Bitmap mBitmap = WUtil.toBitmap(qrCodeWriter.encode(address, BarcodeFormat.QR_CODE, 480, 480));
                new TedPermission(this)
                        .setPermissionListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted() {
                                try {
                                    ContentValues values = new ContentValues();
                                    values.put(MediaStore.Images.Media.TITLE, address);
                                    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                                    Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                                    OutputStream outstream = getContentResolver().openOutputStream(uri);
                                    mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outstream);
                                    outstream.close();

                                    Intent shareIntent = new Intent();
                                    shareIntent.setAction(Intent.ACTION_SEND);
                                    shareIntent.putExtra(Intent.EXTRA_TEXT, address);
                                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                                    shareIntent.setType("image/jpeg");
                                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    startActivity(Intent.createChooser(shareIntent, "send"));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                                Toast.makeText(getBaseContext(), R.string.error_permission, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .setRationaleMessage(getString(R.string.str_permission_qr))
                        .check();

            } catch (WriterException e) {
                e.printStackTrace();
            }
        }
    }

    public void onShareType(String address) {
        Bundle bundle = new Bundle();
        bundle.putString("address", address);
        Dialog_ShareType add = Dialog_ShareType.newInstance(bundle);
        add.setCancelable(true);
        getSupportFragmentManager().beginTransaction().add(add, "dialog").commitNowAllowingStateLoss();
    }

    public void onDeleteAccount(long id) {
        new PushUpdateTask(getBaseApplication(), null, mAccount, getBaseDao().getFCMToken(), false).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        try {
            CryptoHelper.deleteKey(getString(R.string.key_mnemonic) + getBaseDao().onSelectAccount(""+id).uuid);
        } catch (Exception e) { }
        getBaseDao().onDeleteAccount(""+id);
        getBaseDao().onSelectBalance(id);
        getBaseDao().onDeleteBondingStates(id);
        getBaseDao().onDeleteUnbondingStates(id);

        if(getBaseDao().onSelectAccounts().size() > 0) {
            getBaseDao().setLastUser(getBaseDao().onSelectAccounts().get(0).id);
            onStartMainActivity(0);
        } else {
            getBaseDao().setLastUser(-1);
            Intent intent = new Intent(this, IntroActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

    public void onAddMnemonicForAccount() {
        startActivity(new Intent(BaseActivity.this, RestoreActivity.class));
    }

    public void onCancelWithVesting() {

    }

    public void onUpdateUserAlarm(Account account, boolean useAlarm) {
        new PushUpdateTask(getBaseApplication(), this, account, getBaseDao().getFCMToken(), useAlarm).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private CardView mPushBody;
    private ImageView mPushType, mPushClose;
    private TextView mPushTitle, mPushMsg;


    public void onDisplayNotification(Intent intent) {
        if (!(this instanceof PasswordSetActivity) && !(this instanceof PasswordCheckActivity) && !(this instanceof IntroActivity) && !(this instanceof AppLockActivity)) {
            Banner.make(mRootview, this, Banner.TOP, R.layout.foreground_push);
            mPushBody = Banner.getInstance().getBannerView().findViewById(R.id.push_body);
            mPushType = Banner.getInstance().getBannerView().findViewById(R.id.push_type);
            mPushClose = Banner.getInstance().getBannerView().findViewById(R.id.push_close);
            mPushTitle = Banner.getInstance().getBannerView().findViewById(R.id.push_title);
            mPushMsg = Banner.getInstance().getBannerView().findViewById(R.id.push_msg);

            if (intent.getStringExtra("type").equals("sent")) {
                mPushType.setImageDrawable(getResources().getDrawable(R.drawable.ic_notifications_send));
                mPushTitle.setTextColor(getColor(R.color.colorNotiSend));
            } else if (intent.getStringExtra("type").equals("received")) {
                mPushType.setImageDrawable(getResources().getDrawable(R.drawable.ic_notifications_receive));
                mPushTitle.setTextColor(getColor(R.color.colorNotiReceive));
            } else {
                return;
            }
            mPushTitle.setText(intent.getStringExtra("title"));
            mPushMsg.setText(intent.getStringExtra("Body"));

            bannerClickListener(intent.getStringExtra("pushNotifyto"));
            Banner.getInstance().setCustomAnimationStyle(R.style.topAnimation);
            Banner.getInstance().setDuration(4000);
            Banner.getInstance().show();
        }
    }

    private void bannerClickListener(String address){
        mPushBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Account account = getBaseDao().onSelectExistAccount2(address);
                if (account != null) {
                    getBaseDao().setLastUser(account.id);
                    onStartMainActivity(2);
                }
            }
        });

        mPushClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Banner.getInstance().dismissBanner();
            }
        });
    }



    public void onFetchAccountInfo(FetchCallBack callback) {
        if (mTaskCount > 0) {
            callback.fetchBusy();
        }

        ArrayList<Account> accounts = new ArrayList<Account>();
        accounts.add(mAccount);
        mFetchCallback = callback;
        mOtherValidators.clear();
        mAllValidators.clear();
        getBaseDao().mStakingPool = null;
        getBaseDao().mIrisStakingPool = null;
        getBaseDao().mInflation = BigDecimal.ZERO;
        getBaseDao().mProvisions = BigDecimal.ZERO;


        if (mBaseChain.equals(COSMOS_MAIN)) {
            mTaskCount = 9;
            new AllValidatorInfoTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new UnbondingValidatorInfoTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new UnbondedValidatorInfoTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new AccountInfoTask(getBaseApplication(), this, mAccount).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new BondingStateTask(getBaseApplication(), this, mAccount).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            new UnBondingStateTask(getBaseApplication(), this, mAccount).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new SingleInflationTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new SingleProvisionsTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new SingleStakingPoolTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        } else if (mBaseChain.equals(BaseChain.IRIS_MAIN)) {
            mTaskCount = 7;
            new AllValidatorInfoTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new AccountInfoTask(getBaseApplication(), this, mAccount).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new BondingStateTask(getBaseApplication(), this, mAccount).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new UnBondingStateTask(getBaseApplication(), this, mAccount).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new IrisRewardTask(getBaseApplication(), this, mAccount).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            new IrisTokenListTask(getBaseApplication(), this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new IrisPoolTask(getBaseApplication(), this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        } else if (mBaseChain.equals(BaseChain.BNB_MAIN) || mBaseChain.equals(BaseChain.BNB_TEST) ) {
            mTaskCount = 3;
            getBaseDao().mBnbTokens.clear();
            new AccountInfoTask(getBaseApplication(), this, mAccount).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new BnbTokenListTask(getBaseApplication(), this, mAccount).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new BnbMiniTokenListTask(getBaseApplication(), this, mAccount).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            new BnbFeesTask(getBaseApplication(), this, mBaseChain).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        } else if (mBaseChain.equals(KAVA_MAIN)) {
            mTaskCount = 13;
            new AllValidatorInfoTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new UnbondingValidatorInfoTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new UnbondedValidatorInfoTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new AccountInfoTask(getBaseApplication(), this, mAccount).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new BondingStateTask(getBaseApplication(), this, mAccount).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            new UnBondingStateTask(getBaseApplication(), this, mAccount).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new SingleInflationTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new SingleProvisionsTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new SingleStakingPoolTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new KavaCdpParamTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            new KavaPriceParamTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new KavaIncentiveParamTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            //TODO hard code
            new KavaIncentiveRewardTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain), mAccount, "bnb").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        } else if (mBaseChain.equals(KAVA_TEST)) {
            mTaskCount = 13;
            new AllValidatorInfoTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new UnbondingValidatorInfoTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new UnbondedValidatorInfoTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new AccountInfoTask(getBaseApplication(), this, mAccount).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new BondingStateTask(getBaseApplication(), this, mAccount).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            new UnBondingStateTask(getBaseApplication(), this, mAccount).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new SingleInflationTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new SingleProvisionsTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new SingleStakingPoolTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new KavaCdpParamTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            new KavaPriceParamTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new KavaIncentiveParamTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            //TODO hard code
            new KavaIncentiveRewardTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain), mAccount, "bnb").executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        } else if (mBaseChain.equals(IOV_MAIN) || mBaseChain.equals(IOV_TEST)) {
            mTaskCount = 11;
            new AllValidatorInfoTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new UnbondingValidatorInfoTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new UnbondedValidatorInfoTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new AccountInfoTask(getBaseApplication(), this, mAccount).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new BondingStateTask(getBaseApplication(), this, mAccount).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            new UnBondingStateTask(getBaseApplication(), this, mAccount).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new SingleInflationTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new SingleProvisionsTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new SingleStakingPoolTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new StarNameFeeTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            new StarNameConfigTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } else if (mBaseChain.equals(BAND_MAIN)) {
            mTaskCount = 9;
            new AllValidatorInfoTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new UnbondingValidatorInfoTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new UnbondedValidatorInfoTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new AccountInfoTask(getBaseApplication(), this, mAccount).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new BondingStateTask(getBaseApplication(), this, mAccount).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            new UnBondingStateTask(getBaseApplication(), this, mAccount).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new SingleInflationTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new SingleProvisionsTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new SingleStakingPoolTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


        } else if (mBaseChain.equals(BaseChain.OK_TEST)) {
            mTaskCount = 6;
            getBaseDao().mOkDeposit = null;
            getBaseDao().mOkWithdraw = null;
            getBaseDao().mOkTokenList = null;
            new AllValidatorInfoTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new AccountInfoTask(getBaseApplication(), this, mAccount).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new OkAccountTokenTask(getBaseApplication(), this, mAccount, mBaseChain).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            new OkTokenListTask(getBaseApplication(), this, mBaseChain).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new OkDepositTask(getBaseApplication(), this, mAccount, mBaseChain).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new OkWithdrawTask(getBaseApplication(), this, mAccount, mBaseChain).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        } else if (mBaseChain.equals(BaseChain.CERTIK_TEST)) {
            mTaskCount = 9;
            new AllValidatorInfoTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new UnbondingValidatorInfoTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new UnbondedValidatorInfoTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new AccountInfoTask(getBaseApplication(), this, mAccount).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new BondingStateTask(getBaseApplication(), this, mAccount).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            new UnBondingStateTask(getBaseApplication(), this, mAccount).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new SingleInflationTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new SingleProvisionsTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            new SingleStakingPoolTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
        onPriceTic(BaseChain.getChain(mAccount.baseChain));
//        return true;
    }

    @Override
    public void onTaskResponse(TaskResult result) {
//        WLog.w("onTaskResponse " + result.taskType + "   " + mTaskCount);
        if(isFinishing()) return;
        if (result.taskType == BaseConstant.TASK_PUSH_STATUS_UPDATE) {
            if (result.isSuccess) {
                mAccount = getBaseDao().onUpdatePushEnabled(mAccount, (boolean)result.resultData);
            }
            invalidateOptionsMenu();
            return;
        }

        mTaskCount--;
        if (result.taskType == BaseConstant.TASK_FETCH_ACCOUNT) {
            mAccount = getBaseDao().onSelectAccount(getBaseDao().getLastUser());
            mBalances = getBaseDao().onSelectBalance(mAccount.id);

        } else if (result.taskType == BaseConstant.TASK_FETCH_ALL_VALIDATOR) {
            if (!result.isSuccess) {
                Toast.makeText(getBaseContext(), R.string.error_network_error, Toast.LENGTH_SHORT).show();
                return;
            }
            ArrayList<Validator> temp = (ArrayList<Validator>)result.resultData;
            if (mBaseChain.equals(COSMOS_MAIN) || mBaseChain.equals(KAVA_MAIN) || mBaseChain.equals(KAVA_TEST) ||
                    mBaseChain.equals(BAND_MAIN) || mBaseChain.equals(IOV_MAIN) || mBaseChain.equals(IOV_TEST) || mBaseChain.equals(CERTIK_TEST)) {
                if (temp != null) { mTopValidators = temp; }
            } else if (mBaseChain.equals(BaseChain.IRIS_MAIN) || mBaseChain.equals(BaseChain.OK_TEST)) {
                mTopValidators = WUtil.getTopVals(temp);
                mOtherValidators = WUtil.getOthersVals(temp);
            }

        } else if (result.taskType == BaseConstant.TASK_FETCH_UNBONDING_VALIDATOR || result.taskType == BaseConstant.TASK_FETCH_UNBONDED_VALIDATOR) {
            ArrayList<Validator> temp = (ArrayList<Validator>)result.resultData;
            if (temp != null) {
                mOtherValidators.addAll(temp);
            }

        } else if (result.taskType == BaseConstant.TASK_FETCH_BONDING_STATE) {
            mBondings = getBaseDao().onSelectBondingStates(mAccount.id);
            if (mBaseChain.equals(COSMOS_MAIN) || mBaseChain.equals(KAVA_MAIN) || mBaseChain.equals(KAVA_TEST) ||
                    mBaseChain.equals(BAND_MAIN) || mBaseChain.equals(IOV_MAIN) || mBaseChain.equals(IOV_TEST) || mBaseChain.equals(CERTIK_TEST)) {
                mTaskCount = mTaskCount + mBondings.size();
                mRewards.clear();
                for(BondingState bonding:mBondings) {
                    new SingleRewardTask(getBaseApplication(), this, mAccount, bonding.validatorAddress).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }

        } else if (result.taskType == BaseConstant.TASK_FETCH_UNBONDING_STATE) {
            mUnbondings = getBaseDao().onSelectUnbondingStates(mAccount.id);

        } else if (result.taskType == BaseConstant.TASK_FETCH_SINGLE_REWARD) {
            Reward reward = (Reward)result.resultData;
            if(reward != null)
                onUpdateReward(reward);

        } else if (result.taskType == BaseConstant.TASK_FETCH_INFLATION) {
            try {
                this.mInflation = new BigDecimal((String)result.resultData);
                getBaseDao().mInflation = new BigDecimal((String)result.resultData);
            } catch (Exception e) {}

        } else if (result.taskType == BaseConstant.TASK_FETCH_PROVISIONS) {
            try {
                this.mProvisions = new BigDecimal((String)result.resultData);
                getBaseDao().mProvisions = new BigDecimal((String)result.resultData);
            } catch (Exception e) {}

        } else if (result.taskType == BaseConstant.TASK_FETCH_STAKING_POOL) {
            try {
                if (mBaseChain.equals(COSMOS_MAIN) || mBaseChain.equals(KAVA_MAIN) || mBaseChain.equals(KAVA_TEST) ||
                        mBaseChain.equals(BAND_MAIN) || mBaseChain.equals(IOV_MAIN) || mBaseChain.equals(IOV_TEST) || mBaseChain.equals(CERTIK_TEST)) {
                    this.mStakingPool = (ResStakingPool)result.resultData;
                    getBaseDao().mStakingPool = (ResStakingPool)result.resultData;
                }
            } catch (Exception e) {}

        } else if (result.taskType == BaseConstant.TASK_IRIS_REWARD) {
            this.mIrisReward = (ResLcdIrisReward)result.resultData;

        } else if (result.taskType == BaseConstant.TASK_IRIS_POOL) {
            this.mIrisStakingPool = (ResLcdIrisPool)result.resultData;
            getBaseDao().mIrisStakingPool = (ResLcdIrisPool)result.resultData;

        } else if (result.taskType == BaseConstant.TASK_FETCH_BNB_TOKENS) {
            ArrayList<BnbToken> tempTokens = (ArrayList<BnbToken>)result.resultData;
            if (tempTokens!= null) {
                for (BnbToken token:tempTokens) {
                    token.type = BnbToken.BNB_TOKEN_TYPE_BEP2;
                    getBaseDao().mBnbTokens.add(token);
                }
            }

        } else if (result.taskType == BaseConstant.TASK_FETCH_BNB_MINI_TOKENS) {
            ArrayList<BnbToken> tempTokens = (ArrayList<BnbToken>)result.resultData;
            if (tempTokens!= null) {
                for (BnbToken token:tempTokens) {
                    token.type = BnbToken.BNB_TOKEN_TYPE_MINI;
                    getBaseDao().mBnbTokens.add(token);
                }
            }

        } else if (result.taskType == BaseConstant.TASK_FETCH_IRIS_TOKENS) {
            mIrisTokens = (ArrayList<IrisToken>)result.resultData;

        } else if (result.taskType == BaseConstant.TASK_FETCH_KAVA_CDP_PARAM) {
            if (result.isSuccess && result.resultData != null) {
                final ResCdpParam.Result cdpParam = (ResCdpParam.Result)result.resultData;
                getBaseDao().mKavaCdpParams = cdpParam;
                getBaseDao().mMyOwenCdp.clear();
                if (cdpParam != null && cdpParam.collateral_params != null && cdpParam.collateral_params.size() > 0) {
                    mTaskCount = mTaskCount + cdpParam.collateral_params.size();
                    for (ResCdpParam.KavaCollateralParam param:getBaseDao().mKavaCdpParams.collateral_params) {
                        new KavaCdpByOwnerTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain), mAccount.address, param).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }
            }

        } else if (result.taskType == TASK_FETCH_KAVA_CDP_OWENER) {
            if (result.isSuccess && result.resultData != null) {
                getBaseDao().mMyOwenCdp.add((ResCdpOwnerStatus.MyCDP)result.resultData);
            }

        } else if (result.taskType == TASK_FETCH_KAVA_PRICE_PARAM) {
            getBaseDao().mKavaTokenPrices.clear();
            if (result.isSuccess && result.resultData != null) {
                final ResKavaPriceParam.KavaPriceParam kavaPriceParam = (ResKavaPriceParam.KavaPriceParam)result.resultData;
                getBaseDao().mKavaTokenPrices.clear();
                if (kavaPriceParam != null && kavaPriceParam.markets != null && kavaPriceParam.markets.size() > 0) {
                    mTaskCount = mTaskCount + kavaPriceParam.markets.size();
                    for (ResKavaPriceParam.KavaPriceMarket market:kavaPriceParam.markets) {
                        new KavaMarketPriceTask(getBaseApplication(), this, BaseChain.getChain(mAccount.baseChain), market.market_id).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }
            }

        } else if (result.taskType == TASK_FETCH_KAVA_TOKEN_PRICE) {
            if (result.isSuccess && result.resultData != null) {
                final ResKavaMarketPrice.Result price = (ResKavaMarketPrice.Result)result.resultData;
                getBaseDao().mKavaTokenPrices.put(price.market_id, price);
            }

        } else if (result.taskType == TASK_FETCH_KAVA_INCENTIVE_PARAM) {
            if (result.isSuccess && result.resultData != null) {
                getBaseDao().mKavaIncentiveParam = (ResKavaIncentiveParam.IncentiveParam)result.resultData;
            }

        } else if (result.taskType == TASK_FETCH_KAVA_INCENTIVE_REWARD) {
            getBaseDao().mKavaUnClaimedIncentiveRewards.clear();
            if (result.isSuccess && result.resultData != null) {
                getBaseDao().mKavaUnClaimedIncentiveRewards = (ArrayList<ResKavaIncentiveReward.KavaUnclaimedIncentiveReward>) result.resultData;
            }

        } else if (result.taskType == TASK_FETCH_BNB_FEES) {
            getBaseDao().mBnbFees.clear();
            if (result.isSuccess && result.resultData != null) {
                getBaseDao().mBnbFees = (ArrayList<ResBnbFee>)result.resultData;
            }

        } else if (result.taskType == TASK_FETCH_OK_ACCOUNT_TOKEN) {
            if (result.isSuccess) {
                mAccount = getBaseDao().onSelectAccount(getBaseDao().getLastUser());
                mBalances = getBaseDao().onSelectBalance(mAccount.id);
            }

        } else if (result.taskType == TASK_FETCH_OK_DEPOSIT) {
            if (result.isSuccess && result.resultData != null) {
                getBaseDao().mOkDeposit = (ResOkDeposit)result.resultData;
            }

        } else if (result.taskType == TASK_FETCH_OK_WITHDRAW) {
            if (result.isSuccess && result.resultData != null) {
                getBaseDao().mOkWithdraw = ((ResOkWithdraw)result.resultData);
            }

        } else if (result.taskType == TASK_FETCH_OK_TOKEN_LIST) {
            if (result.isSuccess && result.resultData != null) {
                getBaseDao().mOkTokenList = ((ResOkTokenList)result.resultData);
            }

        } else if (result.taskType == TASK_FETCH_STARNAME_FEE) {
            if (result.isSuccess && result.resultData != null) {
                getBaseDao().mStarNameFee = ((ResIovFee.IovFee)result.resultData);
            }

        } else if (result.taskType == TASK_FETCH_STARNAME_CONFIG) {
            if (result.isSuccess && result.resultData != null) {
                getBaseDao().mStarNameConfig = ((ResIovConfig.IovConfig)result.resultData);
            }

        }


        mMyValidators.clear();
        if (mTaskCount == 0 &&
                (mBaseChain.equals(COSMOS_MAIN) || mBaseChain.equals(BaseChain.IRIS_MAIN) || mBaseChain.equals(KAVA_MAIN) ||
                        mBaseChain.equals(KAVA_TEST) || mBaseChain.equals(BAND_MAIN) || mBaseChain.equals(IOV_MAIN) || mBaseChain.equals(IOV_TEST) || mBaseChain.equals(CERTIK_TEST))) {
            for(Validator top:mTopValidators) {
                boolean already = false;
                for (BondingState bond:mBondings) {
                    if(bond.validatorAddress.equals(top.operator_address)) {
                        already = true;
                        break;
                    }
                }
                for (UnBondingState unbond:mUnbondings) {
                    if(unbond.validatorAddress.equals(top.operator_address) && !already) {
                        already = true;
                        break;
                    }
                }
                if (already) mMyValidators.add(top);
            }

            for (Validator other:mOtherValidators) {
                boolean already = false;
                for (BondingState bond:mBondings) {
                    if(bond.validatorAddress.equals(other.operator_address)) {
                        already = true;
                        break;
                    }
                }
                for (UnBondingState unbond:mUnbondings) {
                    if(unbond.validatorAddress.equals(other.operator_address) && !already) {
                        already = true;
                        break;
                    }
                }
                if(already) mMyValidators.add(other);
            }
            mAllValidators.addAll(mTopValidators);
            mAllValidators.addAll(mOtherValidators);
            getBaseDao().mAllValidators = this.mAllValidators;
            getBaseDao().mMyValidators = this.mMyValidators;
            getBaseDao().mTopValidators = this.mTopValidators;
            getBaseDao().mOtherValidators = this.mOtherValidators;

            WLog.w("MyValidators " + mMyValidators.size());
            WLog.w("TopValidators " + mTopValidators.size());
            WLog.w("OtherValidators " + mOtherValidators.size());

            WLog.w("mBondings " + mBondings.size());
            WLog.w("mUnbondings " + mUnbondings.size());
            WLog.w("mRewards " + mRewards.size());

            WLog.w("mInflation " + mInflation);
            WLog.w("mProvisions " + mProvisions);
//            WLog.w("mStakingPool " + mStakingPool);
//            WLog.w("mIrisStakingPool " + mIrisStakingPool);

        } else if (mTaskCount == 0 && (mBaseChain.equals(BaseChain.OK_TEST))) {
            getBaseDao().mTopValidators = mTopValidators;
            getBaseDao().mOtherValidators = mOtherValidators;

            mAllValidators.addAll(mTopValidators);
            mAllValidators.addAll(mOtherValidators);
            getBaseDao().mAllValidators = this.mAllValidators;
            getBaseDao().mTopValidators = this.mTopValidators;
            getBaseDao().mOtherValidators = this.mOtherValidators;

//            WLog.w("TopValidators " + mTopValidators.size());
//            WLog.w("OtherValidators " + mOtherValidators.size());
//            WLog.w("mAllValidators " + mAllValidators.size());

        }
        if (mTaskCount == 0 && mFetchCallback != null) {
            mFetchCallback.fetchFinished();
        }
    }

    private void onUpdateReward(Reward reward) {
        if (mRewards == null) mRewards = new ArrayList<>();
        if (mRewards.size() == 0) {
            mRewards.add(reward);
        } else {
            int match = -1;
            for(int i = 0; i < mRewards.size(); i++) {
                if(mRewards.get(i).validatorAddress.equals(reward.validatorAddress)) {
                    match = i; break;
                }
            }
            if(match > 0) {
                mRewards.remove(match);
            }
            mRewards.add(reward);
        }
    }

    public void onPriceTic(final BaseChain chain) {
        if (getBaseDao().getMarket() == 0) {
            ApiClient.getCGCClient(getBaseContext()).getPriceTic(WUtil.getCGCId(chain)).enqueue(new Callback<ResCgcTic>() {
                @Override
                public void onResponse(Call<ResCgcTic> call, Response<ResCgcTic> response) {
                    if(isFinishing()) return;
                    try {
                        getBaseDao().setLastPriceTic(chain, response.body());
                    } catch (Exception e) {
                        if (chain.equals(COSMOS_MAIN)) {
                            getBaseDao().setLastAtomTic(0d);
                            getBaseDao().setLastAtomUpDown(0d);

                        } else if (chain.equals(BaseChain.IRIS_MAIN)) {
                            getBaseDao().setLastIrisTic(0d);
                            getBaseDao().setLastIrisUpDown(0d);

                        } else if (chain.equals(BaseChain.BNB_MAIN) || chain.equals(BaseChain.BNB_TEST)) {
                            getBaseDao().setLastBnbTic(0d);
                            getBaseDao().setLastBnbUpDown(0d);

                        } else if (chain.equals(KAVA_MAIN) || chain.equals(KAVA_TEST)) {
                            getBaseDao().setLastKavaTic(0d);
                            getBaseDao().setLastKavaUpDown(0d);

                        } else if (chain.equals(BAND_MAIN)) {
                            getBaseDao().setLastBandTic(0d);
                            getBaseDao().setLastBandUpDown(0d);

                        } else if (chain.equals(IOV_MAIN) || chain.equals(IOV_TEST)) {
                            getBaseDao().setLastIovTic(0d);
                            getBaseDao().setLastIovUpDown(0d);

                        }
                    }
                }

                @Override
                public void onFailure(Call<ResCgcTic> call, Throwable t) {
                    if (chain.equals(COSMOS_MAIN)) {
                        getBaseDao().setLastAtomTic(0d);
                        getBaseDao().setLastAtomUpDown(0d);

                    } else if (chain.equals(BaseChain.IRIS_MAIN)) {
                        getBaseDao().setLastIrisTic(0d);
                        getBaseDao().setLastIrisUpDown(0d);

                    } else if (chain.equals(BaseChain.BNB_MAIN) || chain.equals(BaseChain.BNB_TEST)) {
                        getBaseDao().setLastBnbTic(0d);
                        getBaseDao().setLastBnbUpDown(0d);

                    } else if (chain.equals(KAVA_MAIN) || chain.equals(KAVA_TEST)) {
                        getBaseDao().setLastKavaTic(0d);
                        getBaseDao().setLastKavaUpDown(0d);

                    } else if (chain.equals(BAND_MAIN)) {
                        getBaseDao().setLastBandTic(0d);
                        getBaseDao().setLastBandUpDown(0d);

                    } else if (chain.equals(IOV_MAIN) || chain.equals(IOV_TEST)) {
                        getBaseDao().setLastIovTic(0d);
                        getBaseDao().setLastIovUpDown(0d);

                    }
                }
            });

        } else if (getBaseDao().getMarket() == 1) {
            ApiClient.getCMCClient(getBaseContext()).getPriceTic(WUtil.getCMCId(chain), getBaseDao().getCurrencyString()).enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if(isFinishing()) return;
                    try {
                        if(response.isSuccessful() && chain.equals(COSMOS_MAIN)) {
                            ResCmcTic mResCmcTic = new Gson().fromJson(response.body(), ResCmcTic.class);
                            getBaseDao().setLastAtomTic(mResCmcTic.getData().getQuotesMap().get(getBaseDao().getCurrencyString()).getPrice());
                            getBaseDao().setLastAtomUpDown(mResCmcTic.getData().getQuotesMap().get(getBaseDao().getCurrencyString()).getPercent_change_24h());

                        } else if (response.isSuccessful() && chain.equals(BaseChain.IRIS_MAIN)) {
                            ResCmcTic mResCmcTic = new Gson().fromJson(response.body(), ResCmcTic.class);
                            getBaseDao().setLastIrisTic(mResCmcTic.getData().getQuotesMap().get(getBaseDao().getCurrencyString()).getPrice());
                            getBaseDao().setLastIrisUpDown(mResCmcTic.getData().getQuotesMap().get(getBaseDao().getCurrencyString()).getPercent_change_24h());

                        } else if (response.isSuccessful() && (chain.equals(BaseChain.BNB_MAIN) || chain.equals(BaseChain.BNB_TEST) )) {
                            ResCmcTic mResCmcTic = new Gson().fromJson(response.body(), ResCmcTic.class);
                            getBaseDao().setLastBnbTic(mResCmcTic.getData().getQuotesMap().get(getBaseDao().getCurrencyString()).getPrice());
                            getBaseDao().setLastBnbUpDown(mResCmcTic.getData().getQuotesMap().get(getBaseDao().getCurrencyString()).getPercent_change_24h());

                        } else if (response.isSuccessful() && (chain.equals(KAVA_MAIN) || chain.equals(KAVA_TEST))) {
                            ResCmcTic mResCmcTic = new Gson().fromJson(response.body(), ResCmcTic.class);
                            getBaseDao().setLastKavaTic(mResCmcTic.getData().getQuotesMap().get(getBaseDao().getCurrencyString()).getPrice());
                            getBaseDao().setLastKavaUpDown(mResCmcTic.getData().getQuotesMap().get(getBaseDao().getCurrencyString()).getPercent_change_24h());

                        }

                    } catch (Exception e) {
                        if (chain.equals(COSMOS_MAIN)) {
                            getBaseDao().setLastAtomTic(0d);
                            getBaseDao().setLastAtomUpDown(0d);

                        } else if (chain.equals(BaseChain.IRIS_MAIN)) {
                            getBaseDao().setLastIrisTic(0d);
                            getBaseDao().setLastIrisUpDown(0d);

                        } else if (chain.equals(BaseChain.BNB_MAIN) || chain.equals(BaseChain.BNB_TEST)) {
                            getBaseDao().setLastBnbTic(0d);
                            getBaseDao().setLastBnbUpDown(0d);

                        } else if (chain.equals(KAVA_MAIN) || chain.equals(KAVA_TEST)) {
                            getBaseDao().setLastKavaTic(0d);
                            getBaseDao().setLastKavaUpDown(0d);

                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    if (chain.equals(COSMOS_MAIN)) {
                        getBaseDao().setLastAtomTic(0d);
                        getBaseDao().setLastAtomUpDown(0d);

                    } else if (chain.equals(BaseChain.IRIS_MAIN)) {
                        getBaseDao().setLastIrisTic(0d);
                        getBaseDao().setLastIrisUpDown(0d);

                    } else if (chain.equals(BaseChain.BNB_MAIN) || chain.equals(BaseChain.BNB_TEST)) {
                        getBaseDao().setLastBnbTic(0d);
                        getBaseDao().setLastBnbUpDown(0d);

                    } else if (chain.equals(KAVA_MAIN) || chain.equals(KAVA_TEST)) {
                        getBaseDao().setLastKavaTic(0d);
                        getBaseDao().setLastKavaUpDown(0d);

                    }

                }
            });
        }
    }


    public boolean isNotificationsEnabled() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
            if (!manager.areNotificationsEnabled()) {
                return false;
            }
            List<NotificationChannel> channels = manager.getNotificationChannels();
            for (NotificationChannel channel : channels) {
                if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                    return false;
                }
            }
            return true;
        } else {
            return NotificationManagerCompat.from(this).areNotificationsEnabled();
        }
    }

    public void onShowPushEnableDialog() {
        Dialog_Push_Enable dialog = new Dialog_Push_Enable();
        dialog.setCancelable(false);
        getSupportFragmentManager().beginTransaction().add(dialog, "wait").commitNowAllowingStateLoss();

    }

    public void onRedirectPushSet() {
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getBaseContext().getPackageName());
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
            intent.putExtra("app_package", getBaseContext().getPackageName());
            intent.putExtra("app_uid", getBaseContext().getApplicationInfo().uid);
        } else {
            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + getBaseContext().getPackageName()));
        }
        getBaseContext().startActivity(intent);
    }

    public void onShowBuyWarnNoKey() {
        Dialog_Buy_Without_Key dialog = Dialog_Buy_Without_Key.newInstance();
        dialog.setCancelable(true);
        getSupportFragmentManager().beginTransaction().add(dialog, "wait").commitNowAllowingStateLoss();
    }

    public void onShowBuySelectFiat() {
        Dialog_Buy_Select_Fiat dialog = Dialog_Buy_Select_Fiat.newInstance();
        dialog.setCancelable(true);
        getSupportFragmentManager().beginTransaction().add(dialog, "wait").commitNowAllowingStateLoss();
    }

    public void onStartMoonpaySignature(String fiat) {
        String query = "?apiKey=" + getString(R.string.moon_pay_public_key);
        if (mBaseChain.equals(COSMOS_MAIN)) {
            query = query + "&currencyCode=atom";
        } else if (mBaseChain.equals(BaseChain.BNB_MAIN)) {
            query = query + "&currencyCode=bnb";
        } else if (mBaseChain.equals(KAVA_MAIN)) {
            query = query + "&currencyCode=kava";
        } else if (mBaseChain.equals(BAND_MAIN)) {
            query = query + "&currencyCode=band";
        }
        query = query + "&walletAddress=" + mAccount.address + "&baseCurrencyCode=" + fiat;
        final String data = query;

        new MoonPayTask(getBaseApplication(), new TaskListener() {
            @Override
            public void onTaskResponse(TaskResult result) {
                if (result.isSuccess) {
                    try {
                        String en = URLEncoder.encode((String)result.resultData, "UTF-8");
                        Intent guideIntent = new Intent(Intent.ACTION_VIEW , Uri.parse(getString(R.string.url_moon_pay) + data + "&signature=" + en));
                        startActivity(guideIntent);
                    }catch (Exception e) {
                        Toast.makeText(getBaseContext(), R.string.error_network_error, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getBaseContext(), R.string.error_network_error, Toast.LENGTH_SHORT).show();
                }
            }
        }, query).executeOnExecutor(AsyncTask.SERIAL_EXECUTOR);
    }

}
