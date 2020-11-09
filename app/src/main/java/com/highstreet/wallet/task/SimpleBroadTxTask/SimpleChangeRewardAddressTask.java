package com.highstreet.wallet.task.SimpleBroadTxTask;

import com.highstreet.wallet.R;
import com.highstreet.wallet.base.BaseApplication;
import com.highstreet.wallet.base.BaseChain;
import com.highstreet.wallet.base.BaseConstant;
import com.highstreet.wallet.cosmos.MsgGenerator;
import com.highstreet.wallet.crypto.CryptoHelper;
import com.highstreet.wallet.dao.Account;
import com.highstreet.wallet.dao.Password;
import com.highstreet.wallet.model.type.Fee;
import com.highstreet.wallet.model.type.Msg;
import com.highstreet.wallet.network.ApiClient;
import com.highstreet.wallet.network.req.ReqBroadCast;
import com.highstreet.wallet.network.res.ResBroadTx;
import com.highstreet.wallet.network.res.ResLcdAccountInfo;
import com.highstreet.wallet.network.res.ResLcdKavaAccountInfo;
import com.highstreet.wallet.task.CommonTask;
import com.highstreet.wallet.task.TaskListener;
import com.highstreet.wallet.task.TaskResult;
import com.highstreet.wallet.utils.WKey;
import com.highstreet.wallet.utils.WLog;
import com.highstreet.wallet.utils.WUtil;

import org.bitcoinj.crypto.DeterministicKey;

import java.util.ArrayList;

import retrofit2.Response;

import static com.highstreet.wallet.base.BaseChain.IOV_MAIN;
import static com.highstreet.wallet.base.BaseChain.IOV_TEST;
import static com.highstreet.wallet.base.BaseChain.getChain;
import static com.highstreet.wallet.base.BaseConstant.ERROR_CODE_BROADCAST;

public class SimpleChangeRewardAddressTask extends CommonTask {

    private Account         mAccount;
    private String          mToRewardAddress;
    private String          mMemo;
    private Fee             mFees;

    public SimpleChangeRewardAddressTask(BaseApplication app, TaskListener listener,
                                         Account mAccount, String mToRewardAddress,
                                         String mMemo, Fee mFees) {
        super(app, listener);
        this.mAccount = mAccount;
        this.mToRewardAddress = mToRewardAddress;
        this.mMemo = mMemo;
        this.mFees = mFees;
        this.mResult.taskType   = BaseConstant.TASK_GEN_TX_SIMPLE_REWARD_ADDRESS_CHANGE;
    }

    /**
     *
     * @param strings
     *  strings[0] : password
     *
     * @return
     */
    @Override
    protected TaskResult doInBackground(String... strings) {
        try {
            Password checkPw = mApp.getBaseDao().onSelectPassword();
            if(!CryptoHelper.verifyData(strings[0], checkPw.resource, mApp.getString(R.string.key_password))) {
                mResult.isSuccess = false;
                mResult.errorCode = BaseConstant.ERROR_CODE_INVALID_PASSWORD;
                return mResult;
            }

            if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.COSMOS_MAIN)) {
                Response<ResLcdAccountInfo> accountResponse = ApiClient.getCosmosChain(mApp).getAccountInfo(mAccount.address).execute();
                if(!accountResponse.isSuccessful()) {
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                    return mResult;
                }
                mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromLcd(mAccount.id, accountResponse.body()));
                mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromLcd(mAccount.id, accountResponse.body()));
                mAccount = mApp.getBaseDao().onSelectAccount(""+mAccount.id);

            } else if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.IRIS_MAIN)) {
                Response<ResLcdAccountInfo> response = ApiClient.getIrisChain(mApp).getBankInfo(mAccount.address).execute();
                if(!response.isSuccessful()) {
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                    return mResult;
                }
                mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromLcd(mAccount.id, response.body()));
                mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromLcd(mAccount.id, response.body()));
                mAccount = mApp.getBaseDao().onSelectAccount(""+mAccount.id);

            } else if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.KAVA_MAIN)) {
                Response<ResLcdKavaAccountInfo> response = ApiClient.getKavaChain(mApp).getAccountInfo(mAccount.address).execute();
                if(!response.isSuccessful()) {
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                    return mResult;
                }
                mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromKavaLcd(mAccount.id, response.body()));
                mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromKavaLcd(mAccount.id, response.body()));
                mAccount = mApp.getBaseDao().onSelectAccount(""+mAccount.id);

            } else if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.BAND_MAIN)) {
                Response<ResLcdAccountInfo> accountResponse = ApiClient.getBandChain(mApp).getAccountInfo(mAccount.address).execute();
                if(!accountResponse.isSuccessful()) {
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                    return mResult;
                }
                mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromLcd(mAccount.id, accountResponse.body()));
                mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromLcd(mAccount.id, accountResponse.body()));
                mAccount = mApp.getBaseDao().onSelectAccount(""+mAccount.id);

            } else if (getChain(mAccount.baseChain).equals(IOV_MAIN)) {
                Response<ResLcdAccountInfo> accountResponse = ApiClient.getIovChain(mApp).getAccountInfo(mAccount.address).execute();
                if(!accountResponse.isSuccessful()) {
                    mResult.errorCode = ERROR_CODE_BROADCAST;
                    return mResult;
                }
                mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromLcd(mAccount.id, accountResponse.body()));
                mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromLcd(mAccount.id, accountResponse.body()));
                mAccount = mApp.getBaseDao().onSelectAccount(""+mAccount.id);

            } else if (getChain(mAccount.baseChain).equals(IOV_TEST)) {
                Response<ResLcdAccountInfo> accountResponse = ApiClient.getIovTestChain(mApp).getAccountInfo(mAccount.address).execute();
                if(!accountResponse.isSuccessful()) {
                    mResult.errorCode = ERROR_CODE_BROADCAST;
                    return mResult;
                }
                mApp.getBaseDao().onUpdateAccount(WUtil.getAccountFromLcd(mAccount.id, accountResponse.body()));
                mApp.getBaseDao().onUpdateBalances(mAccount.id, WUtil.getBalancesFromLcd(mAccount.id, accountResponse.body()));
                mAccount = mApp.getBaseDao().onSelectAccount(""+mAccount.id);

            }

            String entropy = CryptoHelper.doDecryptData(mApp.getString(R.string.key_mnemonic) + mAccount.uuid, mAccount.resource, mAccount.spec);
            DeterministicKey deterministicKey = WKey.getKeyWithPathfromEntropy(BaseChain.getChain(mAccount.baseChain), entropy, Integer.parseInt(mAccount.path), mAccount.newBip44);

            Msg addressChangeMsg = MsgGenerator.genRewardAddressChange(mAccount.address, mToRewardAddress, BaseChain.getChain(mAccount.baseChain));
            ArrayList<Msg> msgs= new ArrayList<>();
            msgs.add(addressChangeMsg);

            ReqBroadCast reqBroadCast = MsgGenerator.getBraodcaseReq(mAccount, msgs, mFees, mMemo, deterministicKey);
            if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.COSMOS_MAIN)) {
                Response<ResBroadTx> response = ApiClient.getCosmosChain(mApp).broadTx(reqBroadCast).execute();
                if(response.isSuccessful() && response.body() != null) {
                    if (response.body().txhash != null) {
                        mResult.resultData = response.body().txhash;
                    }
                    if(response.body().code != null) {
                        WLog.w("response.code() : " + response.body().code);
                        mResult.errorCode = response.body().code;
                        mResult.errorMsg = response.body().raw_log;
                        return mResult;
                    }
                    mResult.isSuccess = true;

                } else {
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.IRIS_MAIN)) {
                Response<ResBroadTx> response = ApiClient.getIrisChain(mApp).broadTx(reqBroadCast).execute();
                if(response.isSuccessful() && response.body() != null) {
                    if (response.body().hash != null) {
                        mResult.resultData = response.body().hash;
                    }
                    if(response.body().check_tx.code != null) {
                        mResult.errorCode = response.body().check_tx.code;
                        mResult.errorMsg = response.body().raw_log;
                        return mResult;
                    }
                    mResult.isSuccess = true;

                } else {
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.KAVA_MAIN)) {
                Response<ResBroadTx> response = ApiClient.getKavaChain(mApp).broadTx(reqBroadCast).execute();
                if(response.isSuccessful() && response.body() != null) {
                    if (response.body().txhash != null) {
                        mResult.resultData = response.body().txhash;
                    }
                    if(response.body().code != null) {
                        mResult.errorCode = response.body().code;
                        mResult.errorMsg = response.body().raw_log;
                        return mResult;
                    }
                    mResult.isSuccess = true;

                } else {
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.BAND_MAIN)) {
                Response<ResBroadTx> response = ApiClient.getBandChain(mApp).broadTx(reqBroadCast).execute();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().txhash != null) {
                        mResult.resultData = response.body().txhash;
                    }
                    if(response.body().code != null) {
                        mResult.errorCode = response.body().code;
                        mResult.errorMsg = response.body().raw_log;
                        return mResult;
                    }
                    mResult.isSuccess = true;

                } else {
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                }

            } else if (BaseChain.getChain(mAccount.baseChain).equals(BaseChain.IOV_MAIN)) {
                Response<ResBroadTx> response = ApiClient.getIovChain(mApp).broadTx(reqBroadCast).execute();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().txhash != null) {
                        mResult.resultData = response.body().txhash;
                    }
                    if(response.body().code != null) {
                        mResult.errorCode = response.body().code;
                        mResult.errorMsg = response.body().raw_log;
                        return mResult;
                    }
                    mResult.isSuccess = true;

                } else {
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                }

            } else if (getChain(mAccount.baseChain).equals(IOV_TEST)) {
                Response<ResBroadTx> response = ApiClient.getIovTestChain(mApp).broadTx(reqBroadCast).execute();
                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().txhash != null) {
                        mResult.resultData = response.body().txhash;
                    }
                    if(response.body().code != null) {
                        mResult.errorCode = response.body().code;
                        mResult.errorMsg = response.body().raw_log;
                        return mResult;
                    }
                    mResult.isSuccess = true;

                } else {
                    mResult.errorCode = BaseConstant.ERROR_CODE_BROADCAST;
                }

            }


        } catch (Exception e) {
            if(BaseConstant.IS_SHOWLOG) e.printStackTrace();

        }
        return mResult;
    }
}
