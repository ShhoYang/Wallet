package com.highstreet.wallet.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.highstreet.wallet.R;
import com.highstreet.wallet.base.BaseActivity;
import com.highstreet.wallet.fragment.AlphabetKeyBoardFragment;
import com.highstreet.wallet.fragment.KeyboardFragment;
import com.highstreet.wallet.fragment.NumberKeyBoardFragment;
import com.highstreet.wallet.model.StarNameResource;
import com.highstreet.wallet.model.type.Coin;
import com.highstreet.wallet.model.type.Fee;
import com.highstreet.wallet.model.type.Validator;
import com.highstreet.wallet.task.SimpleBroadTxTask.ReInvestTask;
import com.highstreet.wallet.task.SimpleBroadTxTask.SimpleBnbHtlcRefundTask;
import com.highstreet.wallet.task.SimpleBroadTxTask.SimpleBnbSendTask;
import com.highstreet.wallet.task.SimpleBroadTxTask.SimpleChangeRewardAddressTask;
import com.highstreet.wallet.task.SimpleBroadTxTask.SimpleClaimIncentiveTask;
import com.highstreet.wallet.task.SimpleBroadTxTask.SimpleCreateCdpTask;
import com.highstreet.wallet.task.SimpleBroadTxTask.SimpleDelegateTask;
import com.highstreet.wallet.task.SimpleBroadTxTask.SimpleDeleteAccountTask;
import com.highstreet.wallet.task.SimpleBroadTxTask.SimpleDeleteDomainTask;
import com.highstreet.wallet.task.SimpleBroadTxTask.SimpleDepositCdpTask;
import com.highstreet.wallet.task.SimpleBroadTxTask.SimpleDrawBetCdpTask;
import com.highstreet.wallet.task.SimpleBroadTxTask.SimpleHtlcRefundTask;
import com.highstreet.wallet.task.SimpleBroadTxTask.SimpleOkDepositTask;
import com.highstreet.wallet.task.SimpleBroadTxTask.SimpleOkDirectVoteTask;
import com.highstreet.wallet.task.SimpleBroadTxTask.SimpleOkWithdrawTask;
import com.highstreet.wallet.task.SimpleBroadTxTask.SimpleRedelegateTask;
import com.highstreet.wallet.task.SimpleBroadTxTask.SimpleRegisterAccountTask;
import com.highstreet.wallet.task.SimpleBroadTxTask.SimpleRegisterDomainTask;
import com.highstreet.wallet.task.SimpleBroadTxTask.SimpleRenewAccountTask;
import com.highstreet.wallet.task.SimpleBroadTxTask.SimpleRenewDomainTask;
import com.highstreet.wallet.task.SimpleBroadTxTask.SimpleRepayCdpTask;
import com.highstreet.wallet.task.SimpleBroadTxTask.SimpleReplaceStarNameTask;
import com.highstreet.wallet.task.SimpleBroadTxTask.SimpleRewardTask;
import com.highstreet.wallet.task.SimpleBroadTxTask.SimpleSendTask;
import com.highstreet.wallet.task.SimpleBroadTxTask.SimpleUndelegateTask;
import com.highstreet.wallet.task.SimpleBroadTxTask.SimpleVoteTask;
import com.highstreet.wallet.task.SimpleBroadTxTask.SimpleWithdrawCdpTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.task.UserTask.CheckMnemonicTask;
import com.highstreet.wallet.task.UserTask.CheckPasswordTask;
import com.highstreet.wallet.task.UserTask.DeleteUserTask;
import com.highstreet.wallet.utils.KeyboardListener;
import com.highstreet.wallet.utils.WUtil;
import com.highstreet.wallet.widget.StopViewPager;

import java.util.ArrayList;

import static com.highstreet.wallet.base.BaseChain.BNB_MAIN;
import static com.highstreet.wallet.base.BaseChain.BNB_TEST;
import static com.highstreet.wallet.base.BaseChain.KAVA_MAIN;
import static com.highstreet.wallet.base.BaseChain.KAVA_TEST;
import static com.highstreet.wallet.base.BaseChain.getChain;
import static com.highstreet.wallet.base.BaseConstant.CONST_PW_CHECK_MNEMONIC;
import static com.highstreet.wallet.base.BaseConstant.CONST_PW_DELETE_ACCOUNT;
import static com.highstreet.wallet.base.BaseConstant.CONST_PW_PURPOSE;
import static com.highstreet.wallet.base.BaseConstant.CONST_PW_SIMPLE_CHECK;
import static com.highstreet.wallet.base.BaseConstant.CONST_PW_TX_CLAIM_INCENTIVE;
import static com.highstreet.wallet.base.BaseConstant.CONST_PW_TX_CREATE_CDP;
import static com.highstreet.wallet.base.BaseConstant.CONST_PW_TX_DELETE_ACCOUNT;
import static com.highstreet.wallet.base.BaseConstant.CONST_PW_TX_DELETE_DOMAIN;
import static com.highstreet.wallet.base.BaseConstant.CONST_PW_TX_DEPOSIT_CDP;
import static com.highstreet.wallet.base.BaseConstant.CONST_PW_TX_DRAW_DEBT_CDP;
import static com.highstreet.wallet.base.BaseConstant.CONST_PW_TX_HTLS_REFUND;
import static com.highstreet.wallet.base.BaseConstant.CONST_PW_TX_OK_DEPOSIT;
import static com.highstreet.wallet.base.BaseConstant.CONST_PW_TX_OK_DIRECT_VOTE;
import static com.highstreet.wallet.base.BaseConstant.CONST_PW_TX_OK_WITHDRAW;
import static com.highstreet.wallet.base.BaseConstant.CONST_PW_TX_REGISTER_ACCOUNT;
import static com.highstreet.wallet.base.BaseConstant.CONST_PW_TX_REGISTER_DOMAIN;
import static com.highstreet.wallet.base.BaseConstant.CONST_PW_TX_REINVEST;
import static com.highstreet.wallet.base.BaseConstant.CONST_PW_TX_RENEW_ACCOUNT;
import static com.highstreet.wallet.base.BaseConstant.CONST_PW_TX_RENEW_DOMAIN;
import static com.highstreet.wallet.base.BaseConstant.CONST_PW_TX_REPAY_CDP;
import static com.highstreet.wallet.base.BaseConstant.CONST_PW_TX_REPLACE_STARNAME;
import static com.highstreet.wallet.base.BaseConstant.CONST_PW_TX_SIMPLE_CHANGE_REWARD_ADDRESS;
import static com.highstreet.wallet.base.BaseConstant.CONST_PW_TX_SIMPLE_DELEGATE;
import static com.highstreet.wallet.base.BaseConstant.CONST_PW_TX_SIMPLE_REDELEGATE;
import static com.highstreet.wallet.base.BaseConstant.CONST_PW_TX_SIMPLE_REWARD;
import static com.highstreet.wallet.base.BaseConstant.CONST_PW_TX_SIMPLE_SEND;
import static com.highstreet.wallet.base.BaseConstant.CONST_PW_TX_SIMPLE_UNDELEGATE;
import static com.highstreet.wallet.base.BaseConstant.CONST_PW_TX_VOTE;
import static com.highstreet.wallet.base.BaseConstant.CONST_PW_TX_WITHDRAW_CDP;
import static com.highstreet.wallet.base.BaseConstant.ERROR_CODE_INVALID_PASSWORD;
import static com.highstreet.wallet.base.BaseConstant.TASK_CHECK_MNEMONIC;
import static com.highstreet.wallet.base.BaseConstant.TASK_DELETE_USER;
import static com.highstreet.wallet.base.BaseConstant.TASK_GEN_TX_BNB_HTLC_REFUND;
import static com.highstreet.wallet.base.BaseConstant.TASK_PASSWORD_CHECK;

public class PasswordCheckActivity extends BaseActivity implements KeyboardListener, TaskListener {

    private LinearLayout                mLayerContents;
    private TextView                    mPassowrdTitle, mPassowrdMsg1, mPassowrdMsg2;
    private ImageView[]                 mIvCircle = new ImageView[5];

    private StopViewPager               mViewPager;
    private KeyboardPagerAdapter        mAdapter;

    private String                      mUserInput = "";
    private int                         mPurpose;
    private boolean                     mAskQuite;

    private String                      mTargetAddress;
    private ArrayList<Coin>             mTargetCoins;
    private String                      mTargetMemo;
    private Fee                         mTargetFee;
    private Coin                        mDAmount;
    private Coin                        mUAmount;
    private Validator                   mFromReDelegate;
    private Validator                   mToReDelegate;
    private Coin                        mRAmount;
    private ArrayList<Validator>        mValidators = new ArrayList<>();
    private String                      mNewRewardAddress;

    private Validator                   mReInvestValidator;
    private Coin                        mReInvestAmount;

    private String                      mProposalId;
    private String                      mOpinion;

    private Coin                        mCollateralCoin;
    private Coin                        mPrincipalCoin;
    private Coin                        mPaymentCoin;
    private String                      mSender;
    private String                      mOwner;
    private String                      mDepositor;
    private String                      mCdpDenom;
    private String                      mCollateralType;

    private String                      mSwapId;
    private String                      mClaimDenom;

    private Coin                        mOkStakeCoin;
    private ArrayList<String>           mOKVoteValidator = new ArrayList<>();


    private String                      mDomain;
    private String                      mDomainType;
    private String                      mName;
    private ArrayList<StarNameResource> mResources = new ArrayList();


    private long                        mIdToDelete;
    private long                        mIdToCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_set);
        mLayerContents  = findViewById(R.id.layer_contents);
        mPassowrdTitle  = findViewById(R.id.tv_password_title);
        mPassowrdMsg1   = findViewById(R.id.tv_password_msg1);
        mPassowrdMsg2   = findViewById(R.id.tv_password_msg2);
        mViewPager      = findViewById(R.id.pager_keyboard);
        mPassowrdMsg2.setVisibility(View.INVISIBLE);
        mNeedLeaveTime = false;

        for(int i = 0; i < mIvCircle.length; i++) {
            mIvCircle[i] = findViewById(getResources().getIdentifier("img_circle" + i , "id", getPackageName()));
        }

        mViewPager.setOffscreenPageLimit(2);
        mAdapter = new KeyboardPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);

        mPurpose = getIntent().getIntExtra(CONST_PW_PURPOSE, CONST_PW_SIMPLE_CHECK);

        if (mPurpose != CONST_PW_SIMPLE_CHECK) {
            mAccount = getBaseDao().onSelectAccount(getBaseDao().getLastUser());
            mBaseChain = getChain(mAccount.baseChain);
        }
        mTargetAddress = getIntent().getStringExtra("toAddress");
        mTargetCoins = getIntent().getParcelableArrayListExtra("amount");
        mTargetMemo = getIntent().getStringExtra("memo");
        mTargetFee = getIntent().getParcelableExtra("fee");
        mDAmount = getIntent().getParcelableExtra("dAmount");
        mUAmount = getIntent().getParcelableExtra("uAmount");
        mRAmount = getIntent().getParcelableExtra("rAmount");
        mFromReDelegate = getIntent().getParcelableExtra("fromValidator");
        mToReDelegate = getIntent().getParcelableExtra("toValidator");
        mValidators = getIntent().getParcelableArrayListExtra("validators");
        mNewRewardAddress = getIntent().getStringExtra("newRewardAddress");
        mReInvestValidator = getIntent().getParcelableExtra("reInvestValidator");
        mReInvestAmount = getIntent().getParcelableExtra("reInvestAmount");
        mProposalId = getIntent().getStringExtra("proposal_id");
        mOpinion = getIntent().getStringExtra("opinion");
        mCollateralCoin = getIntent().getParcelableExtra("collateralCoin");
        mPrincipalCoin = getIntent().getParcelableExtra("principalCoin");
        mPaymentCoin = getIntent().getParcelableExtra("payment");
        mSender = getIntent().getStringExtra("sender");
        mOwner = getIntent().getStringExtra("owner");
        mDepositor = getIntent().getStringExtra("depositor");
        mCdpDenom = getIntent().getStringExtra("cdp_denom");
        mDepositor = getIntent().getStringExtra("depositor");
        mCdpDenom = getIntent().getStringExtra("cdp_denom");
        mCollateralType = getIntent().getStringExtra("collateralType");
        mSwapId = getIntent().getStringExtra("swapId");
        mClaimDenom = getIntent().getStringExtra("denom");
        mOkStakeCoin = getIntent().getParcelableExtra("stakeAmount");
        mOKVoteValidator = getIntent().getStringArrayListExtra("voteVal");


        mDomain = getIntent().getStringExtra("domain");
        mDomainType = getIntent().getStringExtra("domainType");
        mName = getIntent().getStringExtra("name");
        mResources = getIntent().getParcelableArrayListExtra("resource");


        mIdToDelete = getIntent().getLongExtra("id", -1);
        mIdToCheck  = getIntent().getLongExtra("checkid", -1);

        onInitView();
    }

    @Override
    public void onBackPressed() {
        if(mUserInput != null && mUserInput.length() > 0) {
            userDeleteKey();
        } else {
            if (mAskQuite) {
                setResult(Activity.RESULT_CANCELED, getIntent());
                finish();
            } else {
                mAskQuite = true;
                Toast.makeText(getBaseContext(), R.string.str_ready_to_quite, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void onInitView() {
        mPassowrdTitle.setText(getString(R.string.str_init_password));
        mPassowrdMsg1.setText(getString(R.string.str_init_password));
        mUserInput = "";

        for(int i = 0; i < mIvCircle.length; i++) {
            mIvCircle[i].setBackground(getDrawable(R.drawable.ic_pass_gr));
        }
        mViewPager.setCurrentItem(0, true);
    }

    @Override
    public void userInsertKey(char input) {
        if(mUserInput == null || mUserInput.length() == 0) {
            mUserInput = String.valueOf(input);

        } else if (mUserInput.length() < 5) {
            mUserInput = mUserInput + input;
        }

        if (mUserInput.length() == 4) {
            mViewPager.setCurrentItem(1, true);

        } else if (mUserInput.length() == 5 && WUtil.checkPasscodePattern(mUserInput)) {
            onFinishInput();

        } else if (mUserInput.length() == 5 && !WUtil.checkPasscodePattern(mUserInput)) {
            onInitView();
            return;
        }

        mAskQuite = false;
        onUpdateCnt();

    }

    @Override
    public void userDeleteKey() {
        if(mUserInput == null || mUserInput.length() <= 0) {
            onBackPressed();
        } else if (mUserInput.length() == 4) {
            mUserInput = mUserInput.substring(0, mUserInput.length()-1);
            mViewPager.setCurrentItem(0, true);
        } else {
            mUserInput = mUserInput.substring(0, mUserInput.length()-1);
        }
        onUpdateCnt();
    }


    private void onFinishInput() {
        if (mPurpose == CONST_PW_SIMPLE_CHECK) {
            onShowWaitDialog();
            new CheckPasswordTask(getBaseApplication(), this).execute(mUserInput);

        } else if (mPurpose == CONST_PW_TX_SIMPLE_SEND) {
            onShowWaitDialog();
            if (getChain(mAccount.baseChain).equals(BNB_MAIN) || getChain(mAccount.baseChain).equals(BNB_TEST)) {
                new SimpleBnbSendTask(getBaseApplication(),
                        this,
                        mAccount,
                        mTargetAddress,
                        mTargetCoins,
                        mTargetMemo,
                        mTargetFee).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mUserInput);
            } else {
                new SimpleSendTask(getBaseApplication(),
                        this,
                        mAccount,
                        mTargetAddress,
                        mTargetCoins,
                        mTargetMemo,
                        mTargetFee).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mUserInput);
            }


        } else if (mPurpose == CONST_PW_TX_SIMPLE_DELEGATE) {
            onShowWaitDialog();
            new SimpleDelegateTask(getBaseApplication(),
                    this,
                    mAccount,
                    mTargetAddress,
                    mDAmount,
                    mTargetMemo,
                    mTargetFee).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mUserInput);

        } else if (mPurpose == CONST_PW_TX_SIMPLE_UNDELEGATE) {
            onShowWaitDialog();
            new SimpleUndelegateTask(getBaseApplication(),
                    this,
                    mAccount,
                    mTargetAddress,
                    mUAmount,
                    mTargetMemo,
                    mTargetFee).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mUserInput);

        } else if (mPurpose == CONST_PW_TX_SIMPLE_REWARD) {
            onShowWaitDialog();
            new SimpleRewardTask(getBaseApplication(),
                    this,
                    mAccount,
                    mValidators,
                    mTargetMemo,
                    mTargetFee).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mUserInput);

        } else if (mPurpose == CONST_PW_DELETE_ACCOUNT) {
            onShowWaitDialog();
            new DeleteUserTask(getBaseApplication(), this).execute(mUserInput);

        } else if (mPurpose == CONST_PW_CHECK_MNEMONIC) {
            onShowWaitDialog();
            new CheckMnemonicTask(getBaseApplication(), this, getBaseDao().onSelectAccount(""+mIdToCheck)).execute(mUserInput);

        } else if (mPurpose == CONST_PW_TX_SIMPLE_REDELEGATE) {
            onShowWaitDialog();
            new SimpleRedelegateTask(getBaseApplication(),
                    this,
                    mAccount,
                    mFromReDelegate,
                    mToReDelegate,
                    mRAmount,
                    mTargetMemo,
                    mTargetFee).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mUserInput);

        } else if (mPurpose == CONST_PW_TX_SIMPLE_CHANGE_REWARD_ADDRESS) {
            onShowWaitDialog();
            new SimpleChangeRewardAddressTask(getBaseApplication(),
                    this,
                    mAccount,
                    mNewRewardAddress,
                    mTargetMemo,
                    mTargetFee).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mUserInput);

        } else if (mPurpose == CONST_PW_TX_REINVEST) {
            onShowWaitDialog();
            new ReInvestTask(getBaseApplication(),
                    this,
                    mAccount,
                    mReInvestValidator.operator_address,
                    mReInvestAmount,
                    mTargetMemo,
                    mTargetFee).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mUserInput);

        } else if (mPurpose == CONST_PW_TX_VOTE) {
            onShowWaitDialog();
            new SimpleVoteTask(getBaseApplication(),
                    this,
                    mAccount,
                    mProposalId,
                    mOpinion,
                    mTargetMemo,
                    mTargetFee).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mUserInput);

        } else if (mPurpose == CONST_PW_TX_CREATE_CDP) {
            onShowWaitDialog();
            new SimpleCreateCdpTask(getBaseApplication(),
                    this,
                    mAccount,
                    mSender,
                    mCollateralCoin,
                    mPrincipalCoin,
                    mTargetMemo,
                    mTargetFee,
                    mCollateralType).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mUserInput);

        } else if (mPurpose == CONST_PW_TX_REPAY_CDP) {
            onShowWaitDialog();
            new SimpleRepayCdpTask(getBaseApplication(),
                    this,
                    mAccount,
                    mSender,
                    mPaymentCoin,
                    mCdpDenom,
                    mTargetMemo,
                    mTargetFee,
                    mCollateralType).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mUserInput);

        } else if (mPurpose == CONST_PW_TX_DRAW_DEBT_CDP) {
            onShowWaitDialog();
            new SimpleDrawBetCdpTask(getBaseApplication(),
                    this,
                    mAccount,
                    mSender,
                    mPrincipalCoin,
                    mCdpDenom,
                    mTargetMemo,
                    mTargetFee,
                    mCollateralType).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mUserInput);

        } else if (mPurpose == CONST_PW_TX_DEPOSIT_CDP) {
            onShowWaitDialog();
            new SimpleDepositCdpTask(getBaseApplication(),
                    this,
                    mAccount,
                    mOwner,
                    mDepositor,
                    mCollateralCoin,
                    mTargetMemo,
                    mTargetFee,
                    mCollateralType).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mUserInput);

        } else if (mPurpose == CONST_PW_TX_WITHDRAW_CDP) {
            onShowWaitDialog();
            new SimpleWithdrawCdpTask(getBaseApplication(),
                    this,
                    mAccount,
                    mOwner,
                    mDepositor,
                    mCollateralCoin,
                    mTargetMemo,
                    mTargetFee,
                    mCollateralType).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mUserInput);

        } else if (mPurpose == CONST_PW_TX_HTLS_REFUND) {
            onShowWaitDialog();
            if (mBaseChain.equals(BNB_MAIN) || mBaseChain.equals(BNB_TEST)) {
                new SimpleBnbHtlcRefundTask(getBaseApplication(),
                        this,
                        mAccount,
                        mSwapId,
                        mTargetMemo).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mUserInput);

            } else if (mBaseChain.equals(KAVA_MAIN) || mBaseChain.equals(KAVA_TEST)) {
                new SimpleHtlcRefundTask(getBaseApplication(),
                        this,
                        mAccount,
                        mSwapId,
                        mTargetMemo,
                        mTargetFee).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mUserInput);
            }

        } else if (mPurpose == CONST_PW_TX_CLAIM_INCENTIVE) {
            new SimpleClaimIncentiveTask(getBaseApplication(),
                    this,
                    mAccount,
                    mClaimDenom,
                    mTargetMemo,
                    mTargetFee).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mUserInput);

        } else if (mPurpose == CONST_PW_TX_OK_DEPOSIT) {
            new SimpleOkDepositTask(getBaseApplication(),
                    this,
                    mAccount,
                    mBaseChain,
                    mOkStakeCoin,
                    mTargetMemo,
                    mTargetFee).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mUserInput);

        } else if (mPurpose == CONST_PW_TX_OK_WITHDRAW) {
            new SimpleOkWithdrawTask(getBaseApplication(),
                    this,
                    mAccount,
                    mBaseChain,
                    mOkStakeCoin,
                    mTargetMemo,
                    mTargetFee).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mUserInput);

        } else if (mPurpose == CONST_PW_TX_OK_DIRECT_VOTE) {
            new SimpleOkDirectVoteTask(getBaseApplication(),
                    this,
                    mAccount,
                    mBaseChain,
                    mOKVoteValidator,
                    mTargetMemo,
                    mTargetFee).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mUserInput);

        } else if (mPurpose == CONST_PW_TX_REGISTER_DOMAIN) {
            new SimpleRegisterDomainTask(getBaseApplication(),
                    this,
                    mAccount,
                    mBaseChain,
                    mDomain,
                    mDomainType,
                    mTargetMemo,
                    mTargetFee).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mUserInput);

        } else if (mPurpose == CONST_PW_TX_REGISTER_ACCOUNT) {
            new SimpleRegisterAccountTask(getBaseApplication(),
                    this,
                    mAccount,
                    mBaseChain,
                    mDomain,
                    mName,
                    mResources,
                    mTargetMemo,
                    mTargetFee).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mUserInput);

        } else if (mPurpose == CONST_PW_TX_DELETE_DOMAIN) {
            new SimpleDeleteDomainTask(getBaseApplication(),
                    this,
                    mAccount,
                    mBaseChain,
                    mDomain,
                    mTargetMemo,
                    mTargetFee).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mUserInput);

        } else if (mPurpose == CONST_PW_TX_DELETE_ACCOUNT) {
            new SimpleDeleteAccountTask(getBaseApplication(),
                    this,
                    mAccount,
                    mBaseChain,
                    mDomain,
                    mName,
                    mTargetMemo,
                    mTargetFee).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mUserInput);

        } else if (mPurpose == CONST_PW_TX_RENEW_DOMAIN) {
            new SimpleRenewDomainTask(getBaseApplication(),
                    this,
                    mAccount,
                    mBaseChain,
                    mDomain,
                    mTargetMemo,
                    mTargetFee).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mUserInput);

        } else if (mPurpose == CONST_PW_TX_RENEW_ACCOUNT) {
            new SimpleRenewAccountTask(getBaseApplication(),
                    this,
                    mAccount,
                    mBaseChain,
                    mDomain,
                    mName,
                    mTargetMemo,
                    mTargetFee).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mUserInput);

        } else if (mPurpose == CONST_PW_TX_REPLACE_STARNAME) {
            new SimpleReplaceStarNameTask(getBaseApplication(),
                    this,
                    mAccount,
                    mBaseChain,
                    mDomain,
                    mName,
                    mResources,
                    mTargetMemo,
                    mTargetFee).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mUserInput);

        }
    }

    private void onShakeView() {
        mLayerContents.clearAnimation();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.shake);
        animation.reset();
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation) {
                onInitView();
            }

            @Override
            public void onAnimationRepeat(Animation animation) { }
        });
        mLayerContents.startAnimation(animation);
    }


    private void onUpdateCnt() {
        if(mUserInput == null)
            mUserInput = "";

        final int inputLength = mUserInput.length();
        for(int i = 0; i < mIvCircle.length; i++) {
            if(i < inputLength)
                mIvCircle[i].setBackground(getDrawable(R.drawable.ic_pass_pu));
            else
                mIvCircle[i].setBackground(getDrawable(R.drawable.ic_pass_gr));
        }
    }

    @Override
    public void onTaskResponse(TaskResult result) {
        if(isFinishing()) return;
        onHideWaitDialog();
        if (result.taskType == TASK_PASSWORD_CHECK) {
            if(result.isSuccess) {
                setResult(Activity.RESULT_OK, getIntent());
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.slide_out_bottom);
            } else {
                onShakeView();
                onInitView();
                Toast.makeText(getBaseContext(), getString(R.string.error_invalid_password), Toast.LENGTH_SHORT).show();
            }

        } else if (result.taskType == TASK_DELETE_USER) {
            if(result.isSuccess) {
                onDeleteAccount(mIdToDelete);
            } else {
                onShakeView();
                onInitView();
                Toast.makeText(getBaseContext(), getString(R.string.error_invalid_password), Toast.LENGTH_SHORT).show();

            }

        } else if (result.taskType == TASK_CHECK_MNEMONIC) {
            if(result.isSuccess) {
                Intent checkintent = new Intent(PasswordCheckActivity.this, MnemonicCheckActivity.class);
                checkintent.putExtra("checkid", mIdToCheck);
                checkintent.putExtra("entropy", String.valueOf(result.resultData));
                startActivity(checkintent);

            } else {
                onShakeView();
                onInitView();
                Toast.makeText(getBaseContext(), getString(R.string.error_invalid_password), Toast.LENGTH_SHORT).show();
            }

        } else {
            if (!result.isSuccess && result.errorCode == ERROR_CODE_INVALID_PASSWORD) {
                onShakeView();
                return;
            }

            if ((mBaseChain.equals(BNB_MAIN) || mBaseChain.equals(BNB_TEST)) && result.taskType == TASK_GEN_TX_BNB_HTLC_REFUND) {
                Intent txIntent = new Intent(PasswordCheckActivity.this, TxDetailActivity.class);
                txIntent.putExtra("isGen", true);
                txIntent.putExtra("isSuccess", result.isSuccess);
                txIntent.putExtra("errorCode", result.errorCode);
                txIntent.putExtra("errorMsg", result.errorMsg);
                String hash = String.valueOf(result.resultData);
                if(!TextUtils.isEmpty(hash))
                    txIntent.putExtra("txHash", hash);
                startActivity(txIntent);

            } else {
                Intent txIntent = new Intent(PasswordCheckActivity.this, TxDetailActivity.class);
                txIntent.putExtra("isGen", true);
                txIntent.putExtra("isSuccess", result.isSuccess);
                txIntent.putExtra("errorCode", result.errorCode);
                txIntent.putExtra("errorMsg", result.errorMsg);
                String hash = String.valueOf(result.resultData);
                if(!TextUtils.isEmpty(hash))
                    txIntent.putExtra("txHash", hash);
                startActivity(txIntent);
            }

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    public class KeyboardPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<KeyboardFragment> mFragments = new ArrayList<>();

        public KeyboardPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragments.clear();
            NumberKeyBoardFragment number = NumberKeyBoardFragment.newInstance();
            number.setListener(PasswordCheckActivity.this);
            mFragments.add(number);

            AlphabetKeyBoardFragment alphabet = AlphabetKeyBoardFragment.newInstance();
            alphabet.setListener(PasswordCheckActivity.this);
            mFragments.add(alphabet);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        public ArrayList<KeyboardFragment> getFragments() {
            return mFragments;
        }
    }

}
